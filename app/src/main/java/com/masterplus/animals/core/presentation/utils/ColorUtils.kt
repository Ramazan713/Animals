package com.masterplus.animals.core.presentation.utils

import androidx.compose.ui.graphics.Color

object ColorUtils {
    fun getImageGradientColors(baseColor: Color = Color.Black): Array<Pair<Float, Color>> {
        return arrayOf(
            0.0f to baseColor.copy(alpha = 0.03f),
            0.06f to baseColor.copy(alpha = 0.10f),
            0.13f to baseColor.copy(alpha = 0.17f),
            0.25f to baseColor.copy(alpha = 0.30f),
            0.50f to baseColor.copy(alpha = 0.45f),
            0.75f to baseColor.copy(alpha = 0.55f),
            1f to baseColor.copy(alpha = 0.60f),
        )
    }

}