package com.masterplus.animals.core.domain.models

import androidx.compose.ui.graphics.vector.ImageVector
import com.masterplus.animals.core.domain.utils.UiColor
import com.masterplus.animals.core.domain.utils.UiText

interface IMenuItemEnum {
    val title: UiText
    val iconInfo: IconInfo?
}

data class IconInfo(
    val imageVector: ImageVector,
    val tintColor: UiColor? = null,
    val description: UiText? = null,
    val tooltip: UiText? = null
)
