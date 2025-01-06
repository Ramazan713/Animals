package com.masterplus.animals.core.shared_features.select_font_size.domain.repo

import com.masterplus.animals.core.shared_features.select_font_size.domain.enums.FontSizeEnum
import kotlinx.coroutines.flow.Flow

interface SelectFontSizeRepo {

    val fontSizeFlow: Flow<FontSizeEnum>

    suspend fun getFontSize(): FontSizeEnum

    suspend fun changeFontSize(fontSizeEnum: FontSizeEnum)
}