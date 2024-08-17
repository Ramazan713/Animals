package com.masterplus.animals.features.settings.presentation.sections

import android.app.LocaleManager
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.os.LocaleListCompat
import com.masterplus.animals.R
import com.masterplus.animals.core.presentation.dialogs.ShowQuestionDialog
import com.masterplus.animals.core.presentation.selections.ShowSelectRadioItemDia
import com.masterplus.animals.core.shared_features.auth.presentation.AuthAction
import com.masterplus.animals.core.shared_features.auth.presentation.AuthState
import com.masterplus.animals.core.shared_features.auth.presentation.LoginDia
import com.masterplus.animals.core.shared_features.auth.presentation.ShowDeleteAccountDia
import com.masterplus.animals.core.shared_features.auth.presentation.ShowQuestionReAuthenticateDia
import com.masterplus.animals.core.shared_features.theme.domain.enums.ThemeEnum
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import com.masterplus.animals.features.settings.presentation.SettingsAction
import com.masterplus.animals.features.settings.presentation.SettingsDialogEvent
import com.masterplus.animals.features.settings.presentation.SettingsState
import java.util.Locale


@Composable
fun ShowSettingDialog(
    dialogEvent: SettingsDialogEvent,
    state: SettingsState,
    onAction: (SettingsAction)->Unit,
    authState: AuthState,
    onAuthEvent: (AuthAction) -> Unit,
){

    val close =  remember(onAction) { {
        onAction(SettingsAction.ShowDialog(null))
    }}
    val context = LocalContext.current

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
                onApproved = { onAction(SettingsAction.ShowDialog(dialogEvent = SettingsDialogEvent.AskReAuthenticateForDeletingAccount)) },
                onClosed = close,
            )
        }

        SettingsDialogEvent.AskReAuthenticateForDeletingAccount -> {
            ShowQuestionReAuthenticateDia(
                onCancel = close,
                onApprove = {
                    onAction(SettingsAction.ShowDialog(dialogEvent = SettingsDialogEvent.ShowReAuthenticateForDeletingAccount))
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

        SettingsDialogEvent.ShowSelectTheme -> {
            ShowSelectRadioItemDia(
                items = ThemeEnum.entries,
                selectedItem = state.themeModel.themeEnum,
                title = stringResource(R.string.choice_theme),
                onClose = close,
                onApprove = {onAction(SettingsAction.SetThemeEnum(it))},
                imageVector = Icons.Default.Palette
            )
        }


        SettingsDialogEvent.ShowSelectLanguage -> {
            ShowSelectRadioItemDia(
                items = LanguageEnum.entries,
                selectedItem = state.language,
                title = "Dil Seç",
                onClose = close,
                onApprove = {
                    onAction(SettingsAction.SetLanguage(it))
                    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(it.code)
                    AppCompatDelegate.setApplicationLocales(appLocale)
                },
                imageVector = Icons.Default.Language
            )
        }
    }
}
