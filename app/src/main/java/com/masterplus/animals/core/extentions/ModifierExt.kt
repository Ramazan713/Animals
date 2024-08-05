package com.masterplus.animals.core.extentions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp

@Composable
fun Modifier.clickableWithoutRipple(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
): Modifier{
    return clickable(
        indication = null,
        interactionSource = remember {
            MutableInteractionSource()
        },
        enabled = enabled,
        role = role,
        onClickLabel = onClickLabel,
        onClick = onClick
    )
}

@Composable
fun Modifier.useBackground(
    backgroundColor: Color?,
    shape: Shape = RectangleShape
): Modifier{
    if(backgroundColor == null) return this
    return this.background(backgroundColor, shape)
}

@Composable
fun Modifier.useBorder(
    borderWidth: Dp?,
    color: Color = MaterialTheme.colorScheme.outlineVariant,
    shape: Shape = RectangleShape
): Modifier{
    if(borderWidth == null) return this
    return this.border(borderWidth,color, shape)
}