package com.masterplus.animals.features.settings.presentation

import com.masterplus.animals.core.domain.utils.UiText

data class SettingsState(
    val isLoading: Boolean = false,
    val dialogEvent: SettingsDialogEvent? = null,
    val message: UiText? = null,
)
