package com.masterplus.animals.core.shared_features.theme.domain.models

import com.masterplus.animals.core.shared_features.theme.domain.enums.ThemeEnum


data class ThemeModel(
    val themeEnum: ThemeEnum = ThemeEnum.defaultValue,
    val useDynamicColor: Boolean = false,
    val enabledDynamicColor: Boolean = false,
)
