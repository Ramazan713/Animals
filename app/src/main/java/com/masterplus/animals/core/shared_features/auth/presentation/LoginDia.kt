package com.masterplus.animals.core.shared_features.auth.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.OAuthProvider
import com.masterplus.animals.R
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.presentation.components.SharedHeader
import com.masterplus.animals.core.presentation.dialogs.BaseDialog
import com.masterplus.animals.core.presentation.dialogs.ShowLoadingDialog
import com.masterplus.animals.core.presentation.utils.ShowLifecycleToastMessage
import com.masterplus.animals.core.shared_features.auth.presentation.components.AuthProvidersComponent
import com.masterplus.animals.core.shared_features.auth.presentation.components.EmailAuthProviderComponent
import com.masterplus.animals.core.shared_features.auth.presentation.components.OrDivider
import com.masterplus.animals.core.shared_features.auth.presentation.handlers.AuthDialogEventHandler
import com.masterplus.animals.core.shared_features.auth.presentation.utils.AuthProviderUtils
import kotlinx.coroutines.launch

@Composable
fun LoginDia(
    onAction: (AuthAction) -> Unit,
    state: AuthState,
    isDarkMode: Boolean,
    onClose: () -> Unit,
){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(state.user){
        if(state.user != null){
            onClose()
        }
    }
    LoginDiaUI(
        onAction = onAction,
        state = state,
        isDarkMode = isDarkMode,
        onClose = onClose,
        onSignInWithOAuthProvider = { oAuthProvider ->
            scope.launch {
                AuthProviderUtils.signInWithFirebaseProvider(context, oAuthProvider).getSuccessData?.let { credential ->
                    onAction(AuthAction.SignInWithCredential(credential)) }
            }
        }
    )
}


@Composable
private fun LoginDiaUI(
    onAction: (AuthAction) -> Unit,
    state: AuthState,
    isDarkMode: Boolean,
    onClose: () -> Unit,
    onSignInWithOAuthProvider: (OAuthProvider) -> Unit,
) {
    var error by remember {
        mutableStateOf<UiText?>(null)
    }
    ShowLifecycleToastMessage(
        message = error
    ) {
        error = null
    }

    BaseDialog(
        onClosed = onClose,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 2.dp)
                .padding(bottom = 16.dp)
        ) {
            SharedHeader(
                title = stringResource(id = R.string.sign_in_c),
                onIconClick = onClose
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .padding(horizontal = 4.dp)
            ) {

                item {
                    EmailAuthProviderComponent(
                        onForgetPassword = { email ->
                            onAction(
                                AuthAction.ShowDialog(
                                    AuthDialogEvent.EnterEmailForResetPassword(
                                        email = email,
                                        onResult = { newEmail->
                                            onAction(AuthAction.ResetPassword(newEmail))
                                        }
                                    )
                            ))
                        },
                        onSignIn = { email, password ->
                            onAction(AuthAction.SignInWithEmail(email, password))
                        },
                        onSignUp = { email, password ->
                            onAction(AuthAction.SignUpWithEmail(email, password))
                        },
                    )
                }

                item {
                    OrDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp)
                    )
                }

                item {
                    AuthProvidersComponent(
                        isDarkMode = isDarkMode,
                        onSignInWithCredential = { credential->
                            onAction(AuthAction.SignInWithCredential(credential))
                        },
                        onSignInWithOAuthProvider = onSignInWithOAuthProvider,
                        onError = {
                            error = it
                        }
                    )
                }
            }
        }


    }

    state.dialogEvent?.let { dialogEvent->
        AuthDialogEventHandler(dialogEvent = dialogEvent, onAction = onAction)
    }

    if(state.isLoading){
        ShowLoadingDialog()
    }
}



@Preview
@Composable
private fun LoginPreview() {
    LoginDiaUI(
        onAction = {},
        state = AuthState(
            isLoading = true
        ),
        isDarkMode = false,
        onClose = {},
        onSignInWithOAuthProvider = {}
    )
}
