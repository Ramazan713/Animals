package com.masterplus.animals.core.shared_features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.masterplus.animals.R
import com.masterplus.animals.core.domain.utils.EmptyDefaultResult
import com.masterplus.animals.core.domain.utils.ErrorText
import com.masterplus.animals.core.domain.utils.Result
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.shared_features.auth.domain.models.User
import com.masterplus.animals.core.shared_features.auth.domain.repo.AuthRepo
import com.masterplus.animals.core.shared_features.auth.domain.use_cases.ValidateEmailUseCase
import com.masterplus.animals.core.shared_features.auth.domain.use_cases.ValidatePasswordUseCase
import com.masterplus.animals.core.shared_features.backup.domain.manager.BackupManager
import com.masterplus.animals.core.shared_features.preferences.domain.SettingsPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepo: AuthRepo,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val backupManager: BackupManager,
    private val settingsPreferences: SettingsPreferences
): ViewModel() {


    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state.asStateFlow()

    init {
        listenUser()
    }

    fun onAction(action: AuthAction){
        when(action){
            is AuthAction.ResetPassword -> {
                validateFields(email = action.email){
                    executeLoadingWithSuccess(
                        call = { authRepo.resetPassword(action.email) },
                        onSuccess = { data ->
                            _state.update { it.copy(
                                isLoading = false,
                                message = data,
                                dialogEvent = null
                            ) }
                        }
                    )
                }
            }
            is AuthAction.SignInWithCredential -> {
                handleSignIn {
                    authRepo.signInWithCredential(action.credential)
                }
            }
            is AuthAction.SignInWithEmail -> {
                validateFields(email = action.email,password = action.password){
                    handleSignIn {
                        authRepo.signInWithEmail(action.email,action.password)
                    }
                }
            }
            is AuthAction.SignUpWithEmail -> {
                validateFields(email = action.email,password = action.password) {
                    handleSignIn {
                        authRepo.signUpWithEmail(action.email,action.password)
                    }
                }
            }
            is AuthAction.SignOut -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    when(val result = signOutExecute(action.backupBeforeSignOut)){
                        is Result.Success->{
                            _state.update { it.copy(message = UiText.Resource(R.string.successfully_log_out)) }
                        }
                        is Result.Error->{
                            _state.update { it.copy(message = result.error.text) }
                        }
                    }
                    _state.update { state-> state.copy(isLoading = false) }
                }
            }

            AuthAction.ClearMessage -> {
                _state.update { it.copy(message = null) }
            }
            is AuthAction.ShowDialog -> {
                _state.update { it.copy(dialogEvent = action.dialogEvent) }
            }
            is AuthAction.DeleteUserWithCredentials -> {
                executeLoadingWithSuccess(
                    call = { authRepo.deleteUser(action.credential) },
                    onSuccess = { data -> _state.update { it.copy(message = data) }}
                )
            }

            is AuthAction.DeleteUserWithEmail -> {
                validateFields(email = action.email,password = action.password){
                    val credential = EmailAuthProvider.getCredential(action.email, action.password)
                    onAction(AuthAction.DeleteUserWithCredentials(credential))
                }
            }

            AuthAction.DeleteAllUserData -> {
                viewModelScope.launch {
                    backupManager.deleteAllLocalUserData(false)
                    _state.update { it.copy(message = UiText.Resource(R.string.successfully_deleted)) }
                }
            }
            AuthAction.ClearUiAction -> {
                _state.update { it.copy(uiAction = null) }
            }

            AuthAction.LoadLastBackup -> {
                loadLastBackup()
            }
        }
    }

    private fun listenUser(){
        authRepo
            .userFlow()
            .onEach { user->
                _state.update { it.copy(user = user) }
            }
            .launchIn(viewModelScope)
    }

    private fun handleSignIn(call: suspend () -> Result<User,ErrorText>){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when(val result = call()){
                is Result.Error -> {
                    _state.update { it.copy(
                        isLoading = false,
                        message = result.getFailureError?.text
                    ) }
                }
                is Result.Success -> {
                    val user = result.data
                    val refreshBackupResult = backupManager.refreshBackupMetas(user.uid)
                    var message: UiText? = state.value.message
                    refreshBackupResult.onFailure {
                        message = UiText.Resource(R.string.backup_files_not_downloaded)
                    }
                    refreshBackupResult.onSuccess {
                        message = UiText.Resource(R.string.successfully_log_in)
                    }

                    val hasBackupMetas = backupManager.hasBackupMetas()
                    val showBackupSectionForLogin = settingsPreferences.getData().showBackupSectionForLogin
                    var uiAction: AuthUiAction? = state.value.uiAction
                    if(hasBackupMetas && showBackupSectionForLogin){
                        uiAction = AuthUiAction.ShowBackupSectionForLogin
                    }

                    _state.update { state->
                        state.copy(
                            isLoading = false,
                            message = message,
                            uiAction = uiAction
                        )
                    }
                }
            }


        }
    }

    private fun loadLastBackup(){
        val user = authRepo.currentUser() ?: return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when(val result = backupManager.downloadLastBackup(user.uid)){
                is Result.Error -> {
                    _state.update { it.copy(message = result.error.text) }
                }
                is Result.Success -> {
                    _state.update { it.copy(
                        uiAction = AuthUiAction.RefreshApp,
                        message = UiText.Resource(R.string.success)
                    ) }
                }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }


    private fun<T> executeLoadingWithSuccess(
        call: suspend () -> Result<T,ErrorText>,
        onSuccess: ((T) -> Unit)? = null
    ){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = call()
            result.onFailure { error ->
                _state.update { it.copy(
                    isLoading = false,
                    message = error.text
                ) }
            }
            result.onSuccess { data ->
                _state.update { it.copy(isLoading = false) }
                onSuccess?.invoke(data)
            }
        }
    }

    private suspend fun signOutExecute(makeBackupBeforeSignOut: Boolean): EmptyDefaultResult{
        if(makeBackupBeforeSignOut){
            val user = authRepo.currentUser() ?: return Result.errorWithUiText(UiText.Resource(R.string.user_not_found))
            val backupResult = backupManager.uploadBackup(user.uid)
            if(backupResult.isError){
                return Result.errorWithUiText(UiText.Resource(R.string.backup_not_executed_try_later))
            }
        }
        authRepo.signOut().onSuccessAsync {
            backupManager.deleteAllLocalUserData(true)
        }
        return Result.Success(Unit)
    }

    private fun validateFields(
        email: String? = null,
        password: String? = null,
        call: suspend () -> Unit
    ){
        viewModelScope.launch {
            val passwordError = validatePasswordUseCase(password)
            val emailError = validateEmailUseCase(email)
            if(passwordError != null || emailError != null){
                _state.update { state->
                    state.copy(message = emailError ?: passwordError)
                }
                return@launch
            }
            call()
        }
    }
}