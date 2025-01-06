package com.masterplus.animals.features.settings.presentation

import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.shared_features.select_font_size.domain.enums.FontSizeEnum
import com.masterplus.animals.core.shared_features.theme.domain.models.ThemeModel
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum

data class SettingsState(
    val themeModel: ThemeModel = ThemeModel(),
    val isLoading: Boolean = false,
    val language: LanguageEnum = LanguageEnum.defaultValue,
    val dialogEvent: SettingsDialogEvent? = null,
    val message: UiText? = null,
    val fontSizeEnum: FontSizeEnum = FontSizeEnum.DEFAULT
)
