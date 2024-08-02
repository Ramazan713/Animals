package com.masterplus.animals.core.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.masterplus.animals.core.domain.models.IMenuItemEnum
import com.masterplus.animals.core.domain.models.IconInfo

@Composable
fun IconLabelRow(
    menuItem: IMenuItemEnum,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color? = null,
    borderWidth: Dp? = null,
    shape: Shape = MaterialTheme.shapes.small,
    paddings: PaddingValues = PaddingValues(horizontal = 4.dp, vertical = 12.dp),
){
    IconLabelRow(
        title = menuItem.title.asString(),
        onClick = onClick,
        iconInfo = menuItem.iconInfo,
        modifier = modifier,
        containerColor = containerColor,
        borderWidth = borderWidth,
        shape = shape,
        paddings = paddings
    )
}



@Composable
fun IconLabelRow(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconInfo: IconInfo? = null,
    containerColor: Color? = null,
    borderWidth: Dp? = null,
    shape: Shape = MaterialTheme.shapes.small,
    paddings: PaddingValues = PaddingValues(horizontal = 4.dp, vertical = 12.dp),
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable { onClick() },
        border = if(borderWidth == null) null else BorderStroke(borderWidth, MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(
            containerColor = containerColor ?: CardDefaults.cardColors().containerColor
        ),
        shape = shape,
    ) {
        Row(
            modifier = Modifier
                .padding(paddings),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(12.dp))
            iconInfo?.let { iconInfo->
                Icon(
                    imageVector = iconInfo.imageVector,
                    contentDescription = iconInfo.description?.asString(),
                    modifier = Modifier
                        .size(32.dp),
                    tint = iconInfo.tintColor?.asColor() ?: LocalContentColor.current
                )
            }

            Spacer(Modifier.width(16.dp))
            Text(
                title,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun IconLabelRowPreview() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        IconLabelRow(
            title = "sample title",
            iconInfo = IconInfo(imageVector = Icons.Default.Add),
            onClick = {}
        )

        IconLabelRow(
            title = "sample title",
            iconInfo = null,
            onClick = {}
        )

        IconLabelRow(
            title = "sample title",
            iconInfo = null,
            onClick = {},
            borderWidth = 2.dp
        )
    }
}