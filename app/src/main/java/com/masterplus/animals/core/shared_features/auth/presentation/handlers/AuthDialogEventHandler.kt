package com.masterplus.animals.core.shared_features.auth.presentation.handlers

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.res.stringResource
import com.masterplus.animals.R
import com.masterplus.animals.core.presentation.dialogs.ShowGetTextDialog
import com.masterplus.animals.core.shared_features.auth.presentation.AuthDialogEvent
import com.masterplus.animals.core.shared_features.auth.presentation.AuthEvent

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun AuthDialogEventHandler(
    dialogEvent: AuthDialogEvent,
    onEvent: (AuthEvent) -> Unit
) {
    when(dialogEvent){
        is AuthDialogEvent.EnterEmailForResetPassword -> {
            ShowGetTextDialog(
                title = stringResource(id = R.string.enter_email_for_reset_password),
                onApproved = dialogEvent.onResult,
                onClosed = {
                    onEvent(AuthEvent.ShowDialog(null))
                },
                content = dialogEvent.email,
            )
        }
    }

}