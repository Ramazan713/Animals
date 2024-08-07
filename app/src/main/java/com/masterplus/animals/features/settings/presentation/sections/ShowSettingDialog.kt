package com.masterplus.animals.features.settings.presentation.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.masterplus.animals.R
import com.masterplus.animals.core.presentation.dialogs.ShowQuestionDialog
import com.masterplus.animals.core.shared_features.auth.presentation.AuthEvent
import com.masterplus.animals.core.shared_features.auth.presentation.AuthState
import com.masterplus.animals.features.settings.presentation.SettingsAction
import com.masterplus.animals.features.settings.presentation.SettingsDialogEvent
import com.masterplus.animals.features.settings.presentation.SettingsState
import com.masterplus.animals.core.shared_features.auth.presentation.LoginDia


@Composable
fun ShowSettingDialog(
    dialogEvent: SettingsDialogEvent,
    state: SettingsState,
    onEvent: (SettingsAction)->Unit,
    authState: AuthState,
    onAuthEvent: (AuthEvent) -> Unit,
){

    val close =  remember(onEvent) { {
        onEvent(SettingsAction.ShowDialog(null))
    }}


    when(dialogEvent){
        is SettingsDialogEvent.AskSignOut -> {
            ShowQuestionDialog(
                title = stringResource(R.string.question_sign_out),
                onClosed = close,
                onApproved = {
                    onAuthEvent(AuthEvent.SignOut(true))
                }
            )
        }
        SettingsDialogEvent.ShowAuthDia -> {
            LoginDia(
                onEvent = onAuthEvent,
                state = authState,
                isDarkMode = false,
                onClose = close,
            )
        }
    }
}
