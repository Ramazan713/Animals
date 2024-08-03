package com.masterplus.animals.core.extentions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role

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