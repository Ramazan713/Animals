package com.masterplus.animals.core.shared_features.savepoint.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.masterplus.animals.R
import com.masterplus.animals.core.presentation.components.DefaultImage
import com.masterplus.animals.core.presentation.selections.CustomDropdownBarMenu
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointItemMenu
import com.masterplus.animals.core.shared_features.savepoint.domain.models.SavePoint
import com.masterplus.animals.core.shared_features.savepoint.presentation.extensions.asReadableModifiedData

@Composable
fun SavePointItem(
    savePoint: SavePoint,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    showImage: Boolean = true,
    showAsRow: Boolean = true,
    onMenuClick: ((SavePointItemMenu) -> Unit)? = null,
){
    val shape = MaterialTheme.shapes.medium

    val backgroundColor = if(isSelected) MaterialTheme.colorScheme.secondaryContainer else
        CardDefaults.cardColors().containerColor

    val currentImageUrl = remember(showImage, savePoint) {
        if(showImage) savePoint.imageData ?: R.drawable.all_animals else null
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

        if(showAsRow){
            Row(
                modifier = Modifier
            ) {
                GetImageSection(
                    imageUrl = currentImageUrl,
                    shape = shape,
                    modifier = Modifier.fillMaxHeight()
                )
                if(currentImageUrl == null){
                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                }
                GetContentSection(
                    savePoint = savePoint,
                    onMenuClick = onMenuClick,
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
                    shape = shape,
                    modifier = Modifier.fillMaxWidth()
                )

                GetContentSection(
                    savePoint = savePoint,
                    onMenuClick = onMenuClick,
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                )

            }
        }
    }
}

@Composable
fun GetImageSection(
    imageUrl: Any?,
    shape: CornerBasedShape,
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
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = savePoint.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 3
            )
            Spacer(
                modifier = Modifier
                    .padding(bottom = 4.dp)
            )

            Text(
                text = "pos: ${savePoint.itemPosIndex.toString()}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "type: ${savePoint.destination.title.asString()}",
                style = MaterialTheme.typography.bodyMedium
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
        if(onMenuClick != null){
            CustomDropdownBarMenu(
                items = SavePointItemMenu.entries,
                onItemChange = onMenuClick
            )
        }
    }


}


@Preview(showBackground = true)
@Composable
private fun SavePointItemPreview() {
    val savePoint = SampleDatas.generateSavePoint().copy(imageData = R.drawable.all_animals)
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(4.dp)
            .verticalScroll(rememberScrollState())
    ) {
        LazyRow(
            modifier = Modifier
        ) {
            item {
                SavePointItem(
                    modifier = Modifier
                        .widthIn(min = 150.dp, max = 200.dp)
                        .fillParentMaxHeight(),
                    savePoint = savePoint,
                    isSelected = false,
                    showAsRow = false,
                    onMenuClick = {

                    },
                    onClick = {},
                )
            }

            item {
                SavePointItem(
                    modifier = Modifier
                        .widthIn(min = 150.dp, max = 200.dp)
                        .fillParentMaxHeight()
                    ,
                    savePoint = savePoint.copy(title = "Title"),
                    isSelected = false,
                    showAsRow = false,
                    showImage = true,
                    onMenuClick = {

                    },
                    onClick = {},
                )
            }
        }

        SavePointItem(
            modifier = Modifier.fillMaxWidth(),
            savePoint = savePoint,
            isSelected = true,
            showImage = false,
            onMenuClick = {

            },
            onClick = {},
        )

        SavePointItem(
            savePoint = savePoint,
            isSelected = false,
            showAsRow = false,
            onMenuClick = {},
            onClick = {},
        )
    }
}
