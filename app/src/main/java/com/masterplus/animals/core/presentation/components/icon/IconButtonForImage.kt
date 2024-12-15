package com.masterplus.animals.core.presentation.components.icon

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


@Composable
fun IconButtonForImage(
    imageVector: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentDescription: String? = null,
    alpha: Float = 0.5f
) {
    FilledTonalIconButton(
        modifier = modifier
            .padding(4.dp),
        colors = IconButtonDefaults.filledTonalIconButtonColors(
            containerColor = IconButtonDefaults.filledTonalIconButtonColors()
                .containerColor.copy(alpha = alpha)
        ),
        onClick = onClick,
        enabled = enabled
    ) {
        Icon(imageVector = imageVector, contentDescription = contentDescription)
    }
}