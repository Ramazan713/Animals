package com.masterplus.animals.core.shared_features.backup.presentation.backup_select

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.animals.R
import com.masterplus.animals.core.domain.constants.K
import com.masterplus.animals.core.domain.constants.KPref
import com.masterplus.animals.core.domain.utils.Result
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.shared_features.auth.domain.repo.AuthRepo
import com.masterplus.animals.core.shared_features.backup.domain.manager.BackupManager
import com.masterplus.animals.core.shared_features.backup.domain.repo.BackupMetaRepo
import com.masterplus.animals.core.shared_features.preferences.domain.AppPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class CloudSelectBackupViewModel(
    private val backupManager: BackupManager,
    private val backupMetaRepo: BackupMetaRepo,
    private var authRepo: AuthRepo,
    private val appPreferences: AppPreferences
): ViewModel() {

    private val _state = MutableStateFlow(SelectBackupState())
    val state: StateFlow<SelectBackupState> = _state.asStateFlow()

    init {
        listenData()
        checkRefreshButtonEnabled()
    }


    fun onEvent(event: SelectBackupEvent){
        when(event){
            is SelectBackupEvent.Refresh -> {
                refreshBackupMetas()
            }
            is SelectBackupEvent.AddTopOfBackup -> {
                downloadBackup(false, addOnLocalData = true)
            }
            is SelectBackupEvent.OverrideBackup -> {
                downloadBackup(true, addOnLocalData = false)
            }
            is SelectBackupEvent.SelectItem -> {
                _state.update { state-> state.copy(selectedItem = event.backupMeta)}
            }
            is SelectBackupEvent.ShowDialog -> {
                _state.update { state->
                    state.copy(
                        showDialog = event.showDialog,
                        dialogEvent = event.dialogEvent
                    )
                }
            }
            is SelectBackupEvent.ClearMessage -> {
                _state.update { it.copy(message = null)}
            }
            is SelectBackupEvent.ClearUiEvent -> {
                _state.update { it.copy(uiEvent = null)}
            }
        }
    }

    private fun downloadBackup(deleteAllData: Boolean, addOnLocalData: Boolean){
        viewModelScope.launch {
            val user = authRepo.currentUser() ?: return@launch
            val selectedBackup = _state.value.selectedItem ?: return@launch
            _state.update { state-> state.copy(isLoading = true)}
            when(val result = backupManager.downloadBackup(user.uid,selectedBackup.fileName,deleteAllData,addOnLocalData)){
                is Result.Error -> {
                    _state.update { it.copy(message = result.error.text)}
                }
                is Result.Success -> {
                    _state.update { state-> state.copy(
                        message = UiText.Resource(R.string.success), uiEvent = BackupSelectUiEvent.RestartApp
                    )}
                }
            }
            _state.update { it.copy(isLoading = false)}
        }
    }

    private fun refreshBackupMetas(){
        viewModelScope.launch {
            authRepo.currentUser()?.let { user->
                _state.update { state-> state.copy(isLoading = true)}
                when(val result = backupManager.refreshBackupMetas(user.uid)){
                    is Result.Error -> {
                        _state.update { it.copy(message = result.error.text)}
                    }
                    is Result.Success -> {
                        val time = Date().time
                        appPreferences.setItem(KPref.backupMetaCounter,time)
                        _state.update { it.copy(isRefreshEnabled = false)}
                        checkRefreshButtonEnabled()
                    }
                }.let {
                    _state.update { it.copy(isLoading = false)}
                }
            }
        }
    }

    private fun checkRefreshButtonEnabled(){
        viewModelScope.launch {
            val currentTime = Date().time
            val pastTime = appPreferences.getItem(KPref.backupMetaCounter)
            val diffInMill = (pastTime + K.backupMetaRefreshMilliSeconds) - currentTime
            if(diffInMill < 0){
                _state.update { it.copy(isRefreshEnabled = true)}
                return@launch
            }
            _state.update { state-> state.copy(isRefreshEnabled = false)}
            object : CountDownTimer(diffInMill,1000){
                override fun onTick(millisUntilFinished: Long) {
                    _state.update { it.copy(
                        refreshSeconds = (millisUntilFinished / 1000).toInt()
                    )}
                }
                override fun onFinish() {
                    _state.update { it.copy(isRefreshEnabled = true)}
                }
            }.start()
        }
    }


    private fun listenData(){
        backupMetaRepo
            .getBackupMetasFlow()
            .onEach { items->
                _state.update { it.copy(items = items)}
            }
            .launchIn(viewModelScope)
    }



}