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
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.OAuthProvider
import com.masterplus.animals.R
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.presentation.components.SharedHeader
import com.masterplus.animals.core.presentation.dialogs.BaseDialog
import com.masterplus.animals.core.presentation.dialogs.ShowLoadingDialog
import com.masterplus.animals.core.presentation.utils.ShowLifecycleToastMessage
import com.masterplus.animals.core.shared_features.auth.presentation.components.AuthProvidersComponent
import com.masterplus.animals.core.shared_features.auth.presentation.components.EmailAuthProviderComponent
import com.masterplus.animals.core.shared_features.auth.presentation.components.EmailAuthProviderStyles
import com.masterplus.animals.core.shared_features.auth.presentation.components.EmailString
import com.masterplus.animals.core.shared_features.auth.presentation.components.OrDivider
import com.masterplus.animals.core.shared_features.auth.presentation.components.PasswordString
import com.masterplus.animals.core.shared_features.auth.presentation.utils.AuthProviderUtils
import kotlinx.coroutines.launch


@Composable
fun ShowDeleteAccountDia(
    onAction: (AuthAction) -> Unit,
    state: AuthState,
    isDarkMode: Boolean,
    onClose: () -> Unit,
) {

    LaunchedEffect(state.user) {
        if(state.user == null){
            onClose()
        }
    }

    ReAuthenticateUserDia(
        state = state,
        isDarkMode = isDarkMode,
        onClose = onClose,
        onReAuthenticateWithCredential = {
            onAction(AuthAction.DeleteUserWithCredentials(it))
        },
        onReAuthenticateWithEmail = { email, password ->
            onAction(AuthAction.DeleteUserWithEmail(email, password))
        }
    )
}


@Composable
fun ReAuthenticateUserDia(
    onReAuthenticateWithCredential: (AuthCredential) -> Unit,
    onReAuthenticateWithEmail: (EmailString, PasswordString) -> Unit,
    state: AuthState,
    isDarkMode: Boolean,
    onClose: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var error by remember {
        mutableStateOf<UiText?>(null)
    }

    ShowLifecycleToastMessage(
        message = error
    ) {
        error = null
    }

    ReAuthenticateUserDiaUI(
        isDarkMode = isDarkMode,
        onClose = onClose,
        onSignInWithOAuthProvider = { oAuthProvider ->
            scope.launch {
                AuthProviderUtils.reAuthenticateWithFirebaseProvider(context, oAuthProvider).also {result ->
                    result.getSuccessData?.let { credential ->
                        onReAuthenticateWithCredential(credential)
                    }
                    result.getFailureError?.let { error = it.text }
                }
            }
        },
        onError = {
            error = it
        },
        onSignInWithCredential = onReAuthenticateWithCredential,
        onReAuthenticateWithEmail = onReAuthenticateWithEmail
    )

    if(state.isLoading){
        ShowLoadingDialog()
    }
}



@Composable
private fun ReAuthenticateUserDiaUI(
    isDarkMode: Boolean,
    onClose: () -> Unit,
    onSignInWithOAuthProvider: (OAuthProvider) -> Unit,
    onSignInWithCredential: (AuthCredential) -> Unit,
    onReAuthenticateWithEmail: (EmailString, PasswordString) -> Unit,
    onError: (UiText) -> Unit,
) {

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
                        onSignIn = onReAuthenticateWithEmail,
                        providerStyles = EmailAuthProviderStyles(
                            showSignUp = false,
                            showForgetPassword = false
                        )
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
                        onSignInWithCredential = onSignInWithCredential,
                        onSignInWithOAuthProvider = onSignInWithOAuthProvider,
                        onError = onError
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun ReAuthenticateUserDiaUIPreview() {
    ReAuthenticateUserDiaUI(
        onClose = {},
        isDarkMode = false,
        onSignInWithOAuthProvider = {},
        onSignInWithCredential = {},
        onError = {},
        onReAuthenticateWithEmail = {x,y->}
    )
}