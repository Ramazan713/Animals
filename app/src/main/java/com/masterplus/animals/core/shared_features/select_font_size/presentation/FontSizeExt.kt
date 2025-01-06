package com.masterplus.animals.core.shared_features.select_font_size.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.masterplus.animals.core.shared_features.select_font_size.domain.enums.FontSizeEnum


@Composable
fun TextStyle.scaleFontSize(fontSizeEnum: FontSizeEnum): TextStyle{
    return copy(
        fontSize = (fontSize.value * fontSizeEnum.scale).sp,
        lineHeight = (lineHeight.value * (1 + (fontSizeEnum.scale - 1) / 2)).sp
    )
}


