package com.masterplus.animals.core.shared_features.auth.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.OAuthProvider
import com.masterplus.animals.R
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.presentation.utils.EventHandler
import com.masterplus.animals.core.shared_features.auth.presentation.utils.AuthProviderUtils
import kotlinx.coroutines.launch

@Composable
fun AuthProvidersComponent(
    isDarkMode: Boolean,
    onSignInWithCredential: (AuthCredential) -> Unit,
    onSignInWithOAuthProvider: (OAuthProvider) -> Unit,
    onError: (UiText) -> Unit,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(12.dp)
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var launchAuthProvider by remember {
        mutableStateOf<OAuthProvider?>(null)
    }

    val twitterProvider = remember {
        OAuthProvider.newBuilder("twitter.com")
    }

    val credentialManager = remember {
        CredentialManager.create(context)
    }

    EventHandler(event = launchAuthProvider) { authProvider ->
        onSignInWithOAuthProvider(authProvider)
        launchAuthProvider = null
    }


    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement
    ) {
        AuthProviderRow(
            resId = R.drawable.google_icon,
            title = stringResource(id = R.string.login_with_n_c,"Google"),
            isDarkMode = isDarkMode,
            onClick = {
                scope.launch {
                    val result = AuthProviderUtils.signInWithGoogle(
                        credentialManager = credentialManager,
                        context = context
                    )
                    result.getSuccessData?.let(onSignInWithCredential)
                    result.getFailureError?.text?.let(onError)
                }
            }
        )

        AuthProviderRow(
            resId = R.drawable.x_icon,
            darkResId = R.drawable.x_icon_dark,
            title = stringResource(id = R.string.login_with_n_c,"X"),
            isDarkMode = isDarkMode,
            onClick = {
                launchAuthProvider = twitterProvider.build()
            }
        )
    }
}