package com.masterplus.animals.features.settings.presentation

sealed interface SettingsAction {

    data object LoadData: SettingsAction

    data class ShowDialog(val dialogEvent: SettingsDialogEvent?): SettingsAction

    data object ClearMessage: SettingsAction

}