package com.masterplus.animals.features.settings.presentation

sealed interface SettingsDialogEvent {

    data object AskSignOut: SettingsDialogEvent

    data object ShowAuthDia: SettingsDialogEvent

    data object AskDeleteAccount: SettingsDialogEvent

    data object AskReAuthenticateForDeletingAccount: SettingsDialogEvent

    data object ShowReAuthenticateForDeletingAccount: SettingsDialogEvent

    data object ShowSelectTheme: SettingsDialogEvent

    data object ShowSelectLanguage: SettingsDialogEvent


    data object ShowSelectBackup: SettingsDialogEvent

    data object ShowCloudBackup: SettingsDialogEvent

    data object AskMakeBackupBeforeSignOut: SettingsDialogEvent

    data object AskDeleteAllData: SettingsDialogEvent

    data class BackupSectionInit(
        val onLoadLastBackup: () -> Unit
    ): SettingsDialogEvent

}