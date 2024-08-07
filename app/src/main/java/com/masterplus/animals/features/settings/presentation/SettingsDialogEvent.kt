package com.masterplus.animals.features.settings.presentation

sealed interface SettingsDialogEvent {

    data object AskSignOut: SettingsDialogEvent

    data object ShowAuthDia: SettingsDialogEvent

    data object AskDeleteAccount: SettingsDialogEvent

    data object AskReAuthenticateForDeletingAccount: SettingsDialogEvent

    data object ShowReAuthenticateForDeletingAccount: SettingsDialogEvent

}