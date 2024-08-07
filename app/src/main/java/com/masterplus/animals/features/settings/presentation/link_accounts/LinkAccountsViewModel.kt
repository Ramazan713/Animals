package com.masterplus.animals.features.settings.presentation.link_accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.EmailAuthProvider
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.shared_features.auth.domain.enums.AuthProviderType
import com.masterplus.animals.core.shared_features.auth.domain.repo.AuthRepo
import com.masterplus.animals.core.shared_features.auth.domain.use_cases.ValidateEmailUseCase
import com.masterplus.animals.core.shared_features.auth.domain.use_cases.ValidatePasswordUseCase
import com.masterplus.animals.features.settings.presentation.link_accounts.models.LinkAccountModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LinkAccountsViewModel(
    private val authRepo: AuthRepo,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
): ViewModel() {

    private val _state = MutableStateFlow(LinkAccountsState())
    val state = _state.asStateFlow()


    init {
        listenAuthProviders()
    }


    fun onAction(action: LinkAccountsAction){
        when(action){
            is LinkAccountsAction.LinkWith -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    val result = authRepo.linkWithCredential(action.credential)
                    _state.update { it.copy(
                        isLoading = false,
                        message = result.getFailureError?.text ?: UiText.Text("Başarılı")
                    ) }
                }
            }
            is LinkAccountsAction.LinkWithEmail -> {
                validateFields(email = action.email,password = action.password) {
                    viewModelScope.launch {
                        _state.update { it.copy(isLoading = true) }
                        val emailCredentials = EmailAuthProvider.getCredential(action.email.trim(), action.password.trim())
                        val result = authRepo.linkWithCredential(emailCredentials)
                        result.onSuccess {
                            _state.update { it.copy(
                                isLoading = false,
                                message = UiText.Text("Başarılı"),
                                dialogEvent = null
                            ) }
                        }
                        result.onFailure { error ->
                            _state.update { it.copy(
                                isLoading = false,
                                message = error.text,
                            ) }
                        }
                    }
                }
            }
            is LinkAccountsAction.ShowDialog -> {
                _state.update { it.copy(
                    dialogEvent = action.dialogEvent
                ) }
            }
            is LinkAccountsAction.UnLinkWith -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    val result = authRepo.unLinkWith(action.providerType)
                    _state.update { it.copy(
                        isLoading = false,
                        message = result.getFailureError?.text ?: UiText.Text("Başarılı")
                    ) }
                }
            }
            LinkAccountsAction.ClearMessage -> {
                _state.update { it.copy(
                    message = null
                ) }
            }
        }
    }


    private fun listenAuthProviders(){
        authRepo
            .userFlow()
            .onStart {
                _state.update { it.copy(
                    isLoading = true
                ) }
            }
            .filterNotNull()
            .map { it.authProviders }
            .onEach { authProviders ->
                val linkedAccounts = authProviders.map { provider ->
                    LinkAccountModel(
                        title = provider.providerType.title,
                        isConnected = true,
                        providerType = provider.providerType,
                        info = (provider.email ?: provider.displayName)?.let { UiText.Text(it) }
                    )
                }
                val allProviderTypes = authProviders.map { it.providerType }
                val unLinkedAccounts = AuthProviderType.entries.filterNot { allProviderTypes.contains(it) }
                    .map {  provider ->
                        LinkAccountModel(
                            title = provider.title,
                            isConnected = false,
                            providerType = provider,
                        )
                    }
                _state.update { it.copy(
                    linkedAccounts = linkedAccounts,
                    unLinkedAccounts = unLinkedAccounts,
                    isLoading = false
                ) }
            }
            .launchIn(viewModelScope)
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