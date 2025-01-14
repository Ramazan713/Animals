package com.masterplus.animals.features.settings.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp


@Composable
fun SettingItem(
    title: String,
    onClick: ()->Unit,
    modifier: Modifier = Modifier,
    subTitle: String? = null,
    imageVector: ImageVector? = null,
    color: Color? = null
){
    val shape = MaterialTheme.shapes.small
    Row(
        modifier = modifier
            .padding(vertical = 1.dp)
            .clip(shape)
            .clickable {
                onClick()
            }
            .padding(vertical = if(subTitle!=null) 12.dp else 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        imageVector?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                modifier = Modifier.padding(start = 8.dp),
                tint = color ?: LocalContentColor.current
            )
        }
        Column(
            modifier = Modifier.weight(1f)
                .padding(horizontal = 16.dp),
        ) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = color ?: MaterialTheme.typography.titleMedium.color
                )
            )
            subTitle?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = color ?: MaterialTheme.typography.bodyMedium.color
                    )
                )
            }
        }
    }
}