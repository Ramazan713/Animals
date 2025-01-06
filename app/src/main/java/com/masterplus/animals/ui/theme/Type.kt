package com.masterplus.animals.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.masterplus.animals.core.shared_features.select_font_size.domain.enums.FontSizeEnum
import com.masterplus.animals.core.shared_features.select_font_size.presentation.scaleFontSize


@Composable
fun Typography.scaleFontSizes(fontSizeEnum: FontSizeEnum): Typography {
    return copy(
        displayLarge = displayLarge.scaleFontSize(fontSizeEnum),
        displayMedium = displayMedium.scaleFontSize(fontSizeEnum),
        displaySmall = displaySmall.scaleFontSize(fontSizeEnum),
        headlineLarge = headlineLarge.scaleFontSize(fontSizeEnum),
        headlineMedium = headlineMedium.scaleFontSize(fontSizeEnum),
        headlineSmall = headlineSmall.scaleFontSize(fontSizeEnum),
        titleLarge = titleLarge.scaleFontSize(fontSizeEnum),
        titleMedium = titleMedium.scaleFontSize(fontSizeEnum),
        titleSmall = titleSmall.scaleFontSize(fontSizeEnum),
        bodyLarge = bodyLarge.scaleFontSize(fontSizeEnum),
        bodyMedium = bodyMedium.scaleFontSize(fontSizeEnum),
        bodySmall = bodySmall.scaleFontSize(fontSizeEnum),
        labelLarge = labelLarge.scaleFontSize(fontSizeEnum),
        labelMedium = labelMedium.scaleFontSize(fontSizeEnum),
        labelSmall = labelSmall.scaleFontSize(fontSizeEnum)
    )
}


// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)