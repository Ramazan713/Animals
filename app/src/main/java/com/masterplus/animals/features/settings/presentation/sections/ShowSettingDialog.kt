package com.masterplus.animals.features.settings.presentation.sections

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
import com.masterplus.animals.core.presentation.selections.ShowSelectBottomMenuItems
import com.masterplus.animals.core.presentation.selections.ShowSelectRadioItemDia
import com.masterplus.animals.core.shared_features.auth.presentation.AuthAction
import com.masterplus.animals.core.shared_features.auth.presentation.AuthState
import com.masterplus.animals.core.shared_features.auth.presentation.LoginDia
import com.masterplus.animals.core.shared_features.auth.presentation.ShowDeleteAccountDia
import com.masterplus.animals.core.shared_features.auth.presentation.ShowQuestionReAuthenticateDia
import com.masterplus.animals.core.shared_features.backup.presentation.backup_select.ShowCloudSelectBackupDia
import com.masterplus.animals.core.shared_features.backup.presentation.cloud_backup.ShowCloudSetting
import com.masterplus.animals.core.shared_features.theme.domain.enums.ThemeEnum
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import com.masterplus.animals.features.settings.presentation.SettingsAction
import com.masterplus.animals.features.settings.presentation.SettingsDialogEvent
import com.masterplus.animals.features.settings.presentation.SettingsState
import com.masterplus.animals.features.settings.presentation.enums.BackupLoadSectionEnum


@Composable
fun ShowSettingDialog(
    dialogEvent: SettingsDialogEvent,
    state: SettingsState,
    onAction: (SettingsAction)->Unit,
    authState: AuthState,
    onAuthAction: (AuthAction) -> Unit,
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
                    onAction(SettingsAction.ShowDialog(SettingsDialogEvent.AskMakeBackupBeforeSignOut))
                }
            )
        }
        SettingsDialogEvent.ShowAuthDia -> {
            LoginDia(
                onAction = onAuthAction,
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
                onAction = onAuthAction,
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

        SettingsDialogEvent.ShowCloudBackup -> {
            ShowCloudSetting(
                onClosed = close,
            )
        }
        SettingsDialogEvent.ShowSelectBackup -> {
            ShowCloudSelectBackupDia(
                onClosed = close,
            )
        }
        is SettingsDialogEvent.AskMakeBackupBeforeSignOut -> {
            ShowQuestionDialog(
                title = stringResource(R.string.question_add_backup),
                content = stringResource(R.string.unsaved_data_may_lose),
                onApproved = { onAuthAction(AuthAction.SignOut(true)) },
                allowDismiss = false,
                positiveTitle = stringResource(R.string.backup_v),
                negativeTitle = stringResource(R.string.not_backup),
                onCancel = {
                    onAuthAction(AuthAction.SignOut(false))
                },
                onClosed = close,
            )
        }
        is SettingsDialogEvent.AskDeleteAllData -> {
            ShowQuestionDialog(
                title = stringResource(R.string.are_sure_to_continue),
                content = stringResource(R.string.all_data_will_remove_not_revartable),
                onApproved = { onAuthAction(AuthAction.DeleteAllUserData) },
                onClosed = close,
            )
        }

        is SettingsDialogEvent.BackupSectionInit -> {
            ShowSelectBottomMenuItems(
                items = BackupLoadSectionEnum.entries,
                onClickItem = { menuItem->
                    close()
                    when(menuItem){
                        BackupLoadSectionEnum.LoadLastBackup -> {
                            dialogEvent.onLoadLastBackup()
                        }
                        BackupLoadSectionEnum.ShowBackupFiles -> {
                            onAction(SettingsAction.ShowDialog(SettingsDialogEvent.ShowSelectBackup))
                        }
                        BackupLoadSectionEnum.NotShowAgain -> {
                            onAction(SettingsAction.NotShowBackupInitDialog)
                        }
                    }
                },
                onClose = close
            )
        }
    }
}
