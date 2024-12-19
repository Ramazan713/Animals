package com.masterplus.animals.features.settings.presentation

import com.masterplus.animals.core.shared_features.theme.domain.enums.ThemeEnum
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum

sealed interface SettingsAction {

    data object LoadData: SettingsAction

    data class ShowDialog(val dialogEvent: SettingsDialogEvent?): SettingsAction

    data class SetDynamicTheme(val useDynamic: Boolean): SettingsAction

    data class SetThemeEnum(val themeEnum: ThemeEnum): SettingsAction

    data class SetLanguage(val language: LanguageEnum): SettingsAction

    data object ClearMessage: SettingsAction

    data object NotShowBackupInitDialog: SettingsAction

}