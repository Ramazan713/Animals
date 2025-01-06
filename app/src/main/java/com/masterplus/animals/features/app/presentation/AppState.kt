package com.masterplus.animals.features.app.presentation

import com.masterplus.animals.core.shared_features.select_font_size.domain.enums.FontSizeEnum

data class AppState(
    val isNetworkConnected: Boolean = true,
    val fontSizeEnum: FontSizeEnum = FontSizeEnum.DEFAULT
)
