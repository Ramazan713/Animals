package com.masterplus.animals.core.shared_features.savepoint.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterplus.animals.R
import com.masterplus.animals.core.presentation.components.image.DefaultImage
import com.masterplus.animals.core.presentation.selections.CustomDropdownBarMenu
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.core.presentation.utils.ShapeUtils
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointItemMenu
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointSaveMode
import com.masterplus.animals.core.shared_features.savepoint.domain.models.SavePoint
import com.masterplus.animals.core.shared_features.savepoint.presentation.extensions.asReadableModifiedData

data class SavePointItemDefaults(
    val titleMaxLineLimit: Int = Int.MAX_VALUE,
    val showImage: Boolean = true,
    val showAsRow: Boolean = true,
    val compactContent: Boolean = false,
    val titleStyle: TextStyle? = null
)


@Composable
fun SavePointItem(
    savePoint: SavePoint,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    itemDefaults: SavePointItemDefaults = SavePointItemDefaults(),
    onMenuClick: ((SavePointItemMenu) -> Unit)? = null,
){
    val shape = MaterialTheme.shapes.medium
    val imageShape = ShapeUtils.getRowStyleShape(itemDefaults.showAsRow, shape)

    val backgroundColor = if(isSelected) MaterialTheme.colorScheme.secondaryContainer else
        CardDefaults.cardColors().containerColor

    val currentImageUrl = remember(itemDefaults.showImage, savePoint) {
        if(itemDefaults.showImage) savePoint.image ?: R.drawable.animals_plants else null
    }

    Card(
        modifier = modifier
            .height(IntrinsicSize.Max)
            .clip(shape)
            .selectable(
                isSelected,
                onClick = onClick
            ),
        border = BorderStroke(1.dp,MaterialTheme.colorScheme.outlineVariant),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        shape = shape
    ) {
        if(itemDefaults.showAsRow){
            Row(
                modifier = Modifier
                    .padding(if(currentImageUrl == null) 4.dp else 0.dp)
            ) {
                GetImageSection(
                    imageUrl = currentImageUrl,
                    shape = imageShape,
                    modifier = Modifier.fillMaxHeight()
                )

                GetContentSection(
                    savePoint = savePoint,
                    onMenuClick = onMenuClick,
                    itemDefaults = itemDefaults,
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                )
            }
        }else{
            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .fillMaxHeight()
            ) {
                GetImageSection(
                    imageUrl = currentImageUrl,
                    shape = imageShape,
                    modifier = Modifier.fillMaxWidth()
                )

                GetContentSection(
                    savePoint = savePoint,
                    onMenuClick = onMenuClick,
                    itemDefaults = itemDefaults,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 0.dp)
                        .padding(bottom = 8.dp)
                )

            }
        }
    }
}

@Composable
fun GetImageSection(
    imageUrl: Any?,
    shape: Shape,
    modifier: Modifier = Modifier,
) {
    imageUrl?.let { imageData ->
        DefaultImage(
            imageData = imageData,
            modifier = modifier
                .size(150.dp)
                .clip(shape)
        )
    }
}

@Composable
private fun GetContentSection(
    savePoint: SavePoint,
    onMenuClick: ((SavePointItemMenu) -> Unit)?,
    itemDefaults: SavePointItemDefaults,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = savePoint.title,
                style = itemDefaults.titleStyle ?: MaterialTheme.typography.titleMedium,
                maxLines = itemDefaults.titleMaxLineLimit,
                overflow = TextOverflow.Ellipsis
            )
            if(onMenuClick != null){
                CustomDropdownBarMenu(
                    items = SavePointItemMenu.entries,
                    onItemChange = onMenuClick
                )
            }else{
                Spacer(modifier = Modifier.padding(vertical = 16.dp))
            }
        }

        SavePointInfoItem(
            compactContent = itemDefaults.compactContent,
            savePoint = savePoint
        )

        Spacer(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .weight(1f)
        )

        Text(
            text = savePoint.asReadableModifiedData(),
            style = MaterialTheme.typography.bodySmall
        )

    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SavePointInfoItem(
    compactContent: Boolean,
    savePoint: SavePoint
) {



    if(compactContent){
        FlowRow(
            maxItemsInEachRow = 2,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "pos: ${savePoint.itemPosIndex + 1}",
                style = MaterialTheme.typography.bodyMedium,
            )

            Text(
                text = "type: ${savePoint.destination.title.asString()}",
                style = MaterialTheme.typography.bodyMedium
            )

            if(savePoint.saveMode.isAuto){
                Text(
                    text = "Auto",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }else{
        Text(
            text = "pos: ${savePoint.itemPosIndex + 1}",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = "type: ${savePoint.destination.title.asString()}",
            style = MaterialTheme.typography.bodyMedium
        )

        if(savePoint.saveMode.isAuto){
            Text(
                text = "Auto",
                style = MaterialTheme.typography.bodyMedium
            )
        }

    }
}


@Preview(showBackground = true)
@Composable
private fun SavePointItemPreview() {
    val savePoint = SampleDatas.generateSavePoint().copy(
        title = "Title".repeat(7),
        saveMode = SavePointSaveMode.Auto
    )
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(4.dp)
//            .verticalScroll(rememberScrollState())
    ) {

//        SavePointItem(
//            modifier = Modifier.fillMaxWidth(),
//            savePoint = savePoint,
//            isSelected = true,
//            itemDefaults = SavePointItemDefaults(
//                showImage = false,
//                compactContent = false,
//                titleMaxLineLimit = 1,
//            ),
//
//            onMenuClick = {},
//            onClick = {},
//        )
//
//        SavePointItem(
//            savePoint = savePoint,
//            isSelected = false,
//            itemDefaults = SavePointItemDefaults(
//                showAsRow = false,
//                compactContent = false,
//                titleMaxLineLimit = 1,
//            ),
//            onMenuClick = {},
//            onClick = {},
//        )
//
//        SavePointItem(
//            savePoint = savePoint,
//            isSelected = false,
//            itemDefaults = SavePointItemDefaults(
//                showAsRow = true,
//                compactContent = false,
//                titleMaxLineLimit = 1,
//            ),
//            onMenuClick = {},
//            onClick = {},
//        )

        SavePointItem(
            savePoint = savePoint,
            isSelected = false,
            itemDefaults = SavePointItemDefaults(
                showAsRow = false,
                compactContent = true,
                titleMaxLineLimit = 1,
            ),
            onMenuClick = {},
            onClick = {},
        )
    }
}
