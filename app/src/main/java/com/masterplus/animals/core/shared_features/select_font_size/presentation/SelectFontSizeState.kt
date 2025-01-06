package com.masterplus.animals.core.shared_features.select_font_size.presentation

import com.masterplus.animals.core.shared_features.select_font_size.domain.enums.FontSizeEnum

data class SelectFontSizeState(
    val lastSavedFontSizeEnum: FontSizeEnum = FontSizeEnum.DEFAULT,
    val currentFontSizeEnum: FontSizeEnum = FontSizeEnum.DEFAULT,
    val anyChanges: Boolean = false,
)
