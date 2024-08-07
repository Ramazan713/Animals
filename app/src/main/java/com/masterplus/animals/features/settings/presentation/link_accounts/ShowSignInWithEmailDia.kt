package com.masterplus.animals.features.settings.presentation.link_accounts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.masterplus.animals.core.presentation.components.SharedHeader
import com.masterplus.animals.core.presentation.dialogs.BaseDialog
import com.masterplus.animals.core.shared_features.auth.presentation.components.EmailAuthProviderComponent
import com.masterplus.animals.core.shared_features.auth.presentation.components.EmailAuthProviderStyles

@Composable
fun ShowSignInWithEmailDia(
    onSignIn: (String, String) -> Unit,
    onClosed: () -> Unit
) {
    BaseDialog(
        onClosed = onClosed
    ) {
        Column(
            modifier = Modifier
                .padding(top = 4.dp, bottom = 16.dp)
                .padding(horizontal = 16.dp)
        ) {
            SharedHeader(
                title = "Add Email Account",
                onIconClick = onClosed
            )
            EmailAuthProviderComponent(
                providerStyles = EmailAuthProviderStyles(
                    showForgetPassword = false,
                    showSignUp = false
                ),
                onSignIn = onSignIn
            )
        }
    }
}