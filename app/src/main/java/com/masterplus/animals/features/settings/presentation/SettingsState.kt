package com.masterplus.animals.features.settings.presentation

import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.shared_features.theme.domain.models.ThemeModel

data class SettingsState(
    val themeModel: ThemeModel = ThemeModel(),
    val isLoading: Boolean = false,
    val dialogEvent: SettingsDialogEvent? = null,
    val message: UiText? = null,
)
