package com.masterplus.animals.features.bio_list.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.masterplus.animals.core.domain.models.AnimalData
import com.masterplus.animals.core.presentation.components.DefaultImage
import com.masterplus.animals.core.presentation.components.OrderText
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.core.presentation.utils.ShapeUtils


@Composable
fun BioCard(
    animalData: AnimalData,
    onMenuButtonClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onUnFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
    orderNum: Int? = null,
    isFavorited: Boolean = false,
    isRow: Boolean = true,
    cornerRadiusDp: Dp = 8.dp,
    contentScale: ContentScale = ContentScale.Crop,
    onClick: (() -> Unit)? = null,
){
    BioCard(
        imageData = animalData.imageUrls.firstOrNull() ?: "",
        name = animalData.name,
        bioDescription = animalData.introduction,
        onMenuButtonClick = onMenuButtonClick,
        onFavoriteClick = onFavoriteClick,
        onUnFavoriteClick = onUnFavoriteClick,
        modifier = modifier,
        orderNum = orderNum,
        isFavorited = isFavorited,
        scientificName = animalData.scientificName,
        imageContentDescription = animalData.name,
        isRow = isRow,
        cornerRadiusDp = cornerRadiusDp,
        contentScale = contentScale,
        onClick = onClick
    )
}




@Composable
fun BioCard(
    imageData: Any,
    name: String,
    bioDescription: String,
    onMenuButtonClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onUnFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
    orderNum: Int? = null,
    isFavorited: Boolean = false,
    scientificName: String? = null,
    imageContentDescription: String? = null,
    isRow: Boolean = true,
    cornerRadiusDp: Dp = 8.dp,
    contentScale: ContentScale = ContentScale.Crop,

    onClick: (() -> Unit)? = null,
) {
    val imageShape = ShapeUtils.getRowStyleShape(isRow, cornerRadiusDp)
    val containerShape = RoundedCornerShape(cornerRadiusDp)

    if(isRow){
        Row(
            modifier = modifier
                .heightIn(min = 150.dp, max = 200.dp)
                .clip(containerShape)
                .clickable(
                    enabled = onClick != null,
                ) {
                    onClick?.invoke()
                },
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            GetImage(
                imageData = imageData,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentDescription = imageContentDescription,
                shape = imageShape,
                contentScale = contentScale,
                orderNum = orderNum,
                isFavorited = isFavorited,
                onFavoriteClick = onFavoriteClick,
                onUnFavoriteClick = onUnFavoriteClick
            )

            GetContent(
                modifier = Modifier.weight(1.3f),
                name = name,
                scientificName = scientificName,
                bioDescription = bioDescription,
                onMenuButtonClick = onMenuButtonClick
            )
        }
    }else{
        Column(
            modifier = modifier
                .clip(containerShape)
                .clickable(
                    enabled = onClick != null,
                ) {
                    onClick?.invoke()
                },
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            GetImage(
                imageData = imageData,
                modifier = Modifier
                    .heightIn(min = 200.dp)
                ,
                contentDescription = imageContentDescription,
                shape = imageShape,
                contentScale = contentScale,
                orderNum = orderNum,
                isFavorited = isFavorited,
                onFavoriteClick = onFavoriteClick,
                onUnFavoriteClick = onUnFavoriteClick
            )

            GetContent(
                modifier = Modifier,
                name = name,
                scientificName = scientificName,
                bioDescription = bioDescription,
                onMenuButtonClick = onMenuButtonClick
            )
        }
    }


}


@Composable
private fun GetImage(
    imageData: Any,
    shape: Shape,
    contentDescription: String?,
    orderNum: Int?,
    isFavorited: Boolean,
    onFavoriteClick: (() -> Unit),
    onUnFavoriteClick: (() -> Unit),
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(Color.Green.copy(alpha = 0.3f))
        ,
        contentAlignment = Alignment.BottomCenter
    ) {

        DefaultImage(
            imageData = imageData,
            modifier = Modifier
                .matchParentSize(),
            contentScale = contentScale,
            contentDescription = contentDescription,
            showErrorIcon = false
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            OrderText(
                order = orderNum
            )

            if(isFavorited){
                FilledTonalIconButton(
                    onClick = onUnFavoriteClick,
                ) {
                    Icon(imageVector = Icons.Filled.Star, contentDescription = null)
                }
            }else{
                FilledTonalIconButton(
                    onClick = onFavoriteClick,
                ) {
                    Icon(imageVector = Icons.Outlined.StarOutline, contentDescription = null)
                }
            }
        }
    }
}


@Composable
private fun GetContent(
    modifier: Modifier = Modifier,
    name: String,
    bioDescription: String,
    scientificName: String?,
    onMenuButtonClick: (() -> Unit)
) {
    Column(
        modifier = modifier
            .padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium
                )
                if(scientificName != null){
                    Text(
                        text = scientificName,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

            }
            IconButton(onClick = onMenuButtonClick) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
            }
        }
        Text(
            text = bioDescription,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            overflow = TextOverflow.Ellipsis,
            maxLines = 10
        )
    }
}



@Preview(showBackground = true)
@Composable
fun BioCardPreviewRow() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        BioCard(
            animalData = SampleDatas.animalData,
            modifier = Modifier.fillMaxWidth(),
            orderNum = 10,
            onClick = {},
            onFavoriteClick = {},
            onMenuButtonClick = {},
            onUnFavoriteClick = {}
        )
    }
}


@Preview(showBackground = true, device = Devices.PHONE)
@Composable
fun BioCardPreviewVertical() {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(200.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(2){
            BioCard(
                animalData = SampleDatas.animalData,
                orderNum = it + 1,
                isRow = false,
                isFavorited = it % 2 == 1,
                onClick = {},
                onFavoriteClick = {},
                onMenuButtonClick = {},
                onUnFavoriteClick = {}
            )
        }
    }
}