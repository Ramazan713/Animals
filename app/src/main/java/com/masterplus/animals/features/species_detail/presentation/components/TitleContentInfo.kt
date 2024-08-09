package com.masterplus.animals.features.species_detail.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.masterplus.animals.features.species_detail.presentation.models.TitleContentModel


@Composable
fun TitleContentInfo(
    titleContent: TitleContentModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = titleContent.title,
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            text = titleContent.content,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.typography.bodyMedium.color.copy(alpha = 0.7f)
            )
        )
    }
}