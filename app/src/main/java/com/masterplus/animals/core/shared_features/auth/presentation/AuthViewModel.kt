package com.masterplus.animals.core.shared_features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.masterplus.animals.core.domain.utils.ErrorText
import com.masterplus.animals.core.domain.utils.Result
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.shared_features.auth.domain.models.User
import com.masterplus.animals.core.shared_features.auth.domain.repo.AuthRepo
import com.masterplus.animals.core.shared_features.auth.domain.use_cases.ValidateEmailUseCase
import com.masterplus.animals.core.shared_features.auth.domain.use_cases.ValidatePasswordUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val authRepo: AuthRepo,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
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
                executeLoadingWithSuccess(
                    call = { authRepo.signOut() },
                    onSuccess = { data ->
                        _state.update { it.copy(message = data) }
                    }
                )
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
            val result = call()
            _state.update { it.copy(
                isLoading = false,
                message = result.getFailureError?.text ?: UiText.Text("Success")
            ) }
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