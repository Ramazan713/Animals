package com.masterplus.animals.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun OrderText(
    order: String?,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier
            .alpha(if(order == null) 0f else 1f)
            .padding(vertical = 2.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f))
            .padding(horizontal = 4.dp),
        text = "$order",
        style = MaterialTheme.typography.titleSmall
    )
}


@Preview(showBackground = true)
@Composable
private fun OrderTextPreview() {
    OrderText(
        order = "2"
    )
}