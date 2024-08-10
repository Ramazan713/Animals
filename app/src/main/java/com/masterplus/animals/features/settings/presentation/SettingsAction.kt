package com.masterplus.animals.features.settings.presentation

import com.masterplus.animals.core.shared_features.theme.domain.enums.ThemeEnum

sealed interface SettingsAction {

    data object LoadData: SettingsAction

    data class ShowDialog(val dialogEvent: SettingsDialogEvent?): SettingsAction

    data class SetDynamicTheme(val useDynamic: Boolean): SettingsAction

    data class SetThemeEnum(val themeEnum: ThemeEnum): SettingsAction


    data object ClearMessage: SettingsAction

}