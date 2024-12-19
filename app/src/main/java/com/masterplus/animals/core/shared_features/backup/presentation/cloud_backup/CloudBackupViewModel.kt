package com.masterplus.animals.core.shared_features.backup.presentation.cloud_backup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.animals.R
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.shared_features.auth.domain.repo.AuthRepo
import com.masterplus.animals.core.shared_features.backup.domain.manager.BackupManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CloudBackupViewModel constructor(
    private val backupManager: BackupManager,
    private val authRepo: AuthRepo
): ViewModel(){

    private val _state = MutableStateFlow(CloudBackupState())
    val state: StateFlow<CloudBackupState> = _state.asStateFlow()


    fun makeBackup(){
        viewModelScope.launch {
            authRepo.currentUser()?.let { user->
                _state.update { state-> state.copy(isLoading = true)}
                val result = backupManager.uploadBackup(user.uid)
                _state.update { it.copy(
                    isLoading = false,
                    message = result.getFailureError?.text ?: UiText.Resource(R.string.success)
                ) }
            }
        }
    }


    fun clearMessage(){
        _state.update { it.copy(message = null)}
    }

}