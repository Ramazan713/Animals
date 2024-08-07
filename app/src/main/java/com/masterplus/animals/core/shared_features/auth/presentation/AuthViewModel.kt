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

    fun onEvent(event: AuthEvent){
        when(event){
            is AuthEvent.ResetPassword -> {
                validateFields(email = event.email){
                    val result = authRepo.resetPassword(event.email)

                }
            }
            is AuthEvent.SignInWithCredential -> {
                handleSignIn {
                    authRepo.signInWithCredential(event.credential)
                }
            }
            is AuthEvent.SignInWithEmail -> {
                validateFields(email = event.email,password = event.password){
                    handleSignIn {
                        authRepo.signInWithEmail(event.email,event.password)
                    }
                }
            }
            is AuthEvent.SignUpWithEmail -> {
                validateFields(email = event.email,password = event.password) {
                    handleSignIn {
                        authRepo.signUpWithEmail(event.email,event.password)
                    }
                }
            }
            is AuthEvent.SignOut -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    val result = authRepo.signOut()
                    _state.update { state-> state.copy(
                        isLoading = false,
                        message = result.getFailureError?.text ?: result.getSuccessData
                    ) }
                }
            }

            AuthEvent.ClearMessage -> {
                _state.update { it.copy(message = null) }
            }
            is AuthEvent.ShowDialog -> {
                _state.update { it.copy(dialogEvent = event.dialogEvent) }
            }
            is AuthEvent.DeleteUserWithCredentials -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    val result = authRepo.deleteUser(event.credential)
                    _state.update { it.copy(
                        isLoading = false,
                        message = result.getSuccessData ?: result.getFailureError?.text
                    ) }
                }
            }

            is AuthEvent.DeleteUserWithEmail -> {
                viewModelScope.launch {
                    validateFields(email = event.email,password = event.password){
                        val credential = EmailAuthProvider.getCredential(event.email, event.password)
                        onEvent(AuthEvent.DeleteUserWithCredentials(credential))
                    }
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