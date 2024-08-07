package com.masterplus.animals.core.shared_features.auth.presentation

import android.app.Activity
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.masterplus.animals.R
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.presentation.components.SharedHeader
import com.masterplus.animals.core.presentation.dialogs.BaseDialog
import com.masterplus.animals.core.presentation.dialogs.ShowLoadingDialog
import com.masterplus.animals.core.presentation.utils.ShowLifecycleToastMessage
import com.masterplus.animals.core.shared_features.auth.presentation.components.AuthProvidersComponent
import com.masterplus.animals.core.shared_features.auth.presentation.components.OrDivider
import com.masterplus.animals.core.shared_features.auth.presentation.handlers.AuthDialogEventHandler
import com.masterplus.animals.core.shared_features.auth.presentation.components.EmailAuthProviderComponent

@Composable
fun LoginDia(
    onEvent: (AuthEvent) -> Unit,
    state: AuthState,
    isDarkMode: Boolean,
    onClose: () -> Unit,
){
    val context = LocalContext.current
    val firebaseAuth = remember {
        FirebaseAuth.getInstance()
    }
    LaunchedEffect(state.user){
        if(state.user != null){
            onClose()
        }
    }
    LoginDiaUI(
        onEvent = onEvent,
        state = state,
        isDarkMode = isDarkMode,
        onClose = onClose,
        onSignInWithOAuthProvider = { oAuthProvider ->
            firebaseAuth.startActivityForSignInWithProvider(context as Activity,oAuthProvider).addOnCompleteListener {
                it.result?.credential?.let { credential ->
                    onEvent(AuthEvent.SignInWithCredential(credential))
                }
            }
        }
    )
}


@Composable
private fun LoginDiaUI(
    onEvent: (AuthEvent) -> Unit,
    state: AuthState,
    isDarkMode: Boolean,
    onClose: () -> Unit,
    onSignInWithOAuthProvider: (OAuthProvider) -> Unit,
) {
    var error by remember {
        mutableStateOf<UiText?>(null)
    }
    val currentOnEvent by rememberUpdatedState(newValue = onEvent)

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
                            onEvent(
                                AuthEvent.ShowDialog(
                                    AuthDialogEvent.EnterEmailForResetPassword(
                                        email = email,
                                        onResult = {newEmail->
                                            onEvent(AuthEvent.ResetPassword(newEmail))
                                        }
                                    )
                            ))
                        },
                        onSignIn = { email, password ->
                            onEvent(AuthEvent.SignInWithEmail(email, password))
                        },
                        onSignUp = { email, password ->
                            onEvent(AuthEvent.SignUpWithEmail(email, password))
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
                            currentOnEvent(AuthEvent.SignInWithCredential(credential))
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
        AuthDialogEventHandler(dialogEvent = dialogEvent, onEvent = onEvent)
    }

    if(state.isLoading){
        ShowLoadingDialog()
    }

}



@Preview
@Composable
private fun LoginPreview() {
    LoginDiaUI(
        onEvent = {},
        state = AuthState(),
        isDarkMode = false,
        onClose = {},
        onSignInWithOAuthProvider = {}
    )
}
