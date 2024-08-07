package com.masterplus.animals.features.settings.presentation.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.masterplus.animals.R
import com.masterplus.animals.core.presentation.dialogs.ShowQuestionDialog
import com.masterplus.animals.core.shared_features.auth.presentation.AuthAction
import com.masterplus.animals.core.shared_features.auth.presentation.AuthState
import com.masterplus.animals.features.settings.presentation.SettingsAction
import com.masterplus.animals.features.settings.presentation.SettingsDialogEvent
import com.masterplus.animals.features.settings.presentation.SettingsState
import com.masterplus.animals.core.shared_features.auth.presentation.LoginDia
import com.masterplus.animals.core.shared_features.auth.presentation.ShowDeleteAccountDia
import com.masterplus.animals.core.shared_features.auth.presentation.ShowQuestionReAuthenticateDia


@Composable
fun ShowSettingDialog(
    dialogEvent: SettingsDialogEvent,
    state: SettingsState,
    onEvent: (SettingsAction)->Unit,
    authState: AuthState,
    onAuthEvent: (AuthAction) -> Unit,
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
                    onAuthEvent(AuthAction.SignOut(true))
                }
            )
        }
        SettingsDialogEvent.ShowAuthDia -> {
            LoginDia(
                onAction = onAuthEvent,
                state = authState,
                isDarkMode = false,
                onClose = close,
            )
        }
        SettingsDialogEvent.AskDeleteAccount -> {
            ShowQuestionDialog(
                title = stringResource(R.string.q_delete_account),
                content = stringResource(id = R.string.delete_account_warning),
                onApproved = { onEvent(SettingsAction.ShowDialog(dialogEvent = SettingsDialogEvent.AskReAuthenticateForDeletingAccount)) },
                onClosed = close,
            )
        }

        SettingsDialogEvent.AskReAuthenticateForDeletingAccount -> {
            ShowQuestionReAuthenticateDia(
                onCancel = close,
                onApprove = {
                    onEvent(SettingsAction.ShowDialog(dialogEvent = SettingsDialogEvent.ShowReAuthenticateForDeletingAccount))
                }
            )
        }

        SettingsDialogEvent.ShowReAuthenticateForDeletingAccount -> {
            ShowDeleteAccountDia(
                onAction = onAuthEvent,
                state = authState,
                isDarkMode = false,
                onClose = close,
            )
        }
    }
}
