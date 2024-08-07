package com.masterplus.animals.features.settings.presentation

sealed interface SettingsDialogEvent {

    data object AskSignOut: SettingsDialogEvent

    data object ShowAuthDia: SettingsDialogEvent
}