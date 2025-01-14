package com.masterplus.animals.core.presentation.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.masterplus.animals.core.domain.models.ImageWithMetadata
import com.masterplus.animals.core.domain.models.SpeciesListDetail
import com.masterplus.animals.core.presentation.components.image.TransitionImage
import com.masterplus.animals.core.presentation.transition.TransitionImageKey
import com.masterplus.animals.core.presentation.transition.TransitionImageType
import com.masterplus.animals.core.presentation.transition.TransitionTextKey
import com.masterplus.animals.core.presentation.transition.TransitionTextType
import com.masterplus.animals.core.presentation.transition.sharedBoundsText
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.core.presentation.utils.ShapeUtils


@Composable
fun SpeciesCard(
    species: SpeciesListDetail,
    onMenuButtonClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onUnFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
    orderNum: String? = null,
    isFavorited: Boolean = false,
    isRow: Boolean = true,
    cornerRadiusDp: Dp = 8.dp,
    contentScale: ContentScale = ContentScale.Crop,
    onClick: (() -> Unit)? = null,
    useTransition: Boolean = true
){
    SpeciesCard(
        image = species.images.firstOrNull()?.image,
        name = species.name,
        speciesDescription = species.introduction,
        onMenuButtonClick = onMenuButtonClick,
        onFavoriteClick = onFavoriteClick,
        onUnFavoriteClick = onUnFavoriteClick,
        modifier = modifier,
        orderNum = orderNum,
        isFavorited = isFavorited,
        scientificName = species.scientificName,
        imageContentDescription = species.name,
        isRow = isRow,
        cornerRadiusDp = cornerRadiusDp,
        contentScale = contentScale,
        onClick = onClick,
        transitionKey = if(useTransition) species.images.firstOrNull()?.let {
            TransitionImageKey(
                id = it.speciesId,
                extra = it.image.id?.toString(),
                imageType = TransitionImageType.SpeciesImages,
                kingdomType = species.kingdomType
            )
        } else null
    )
}




@Composable
fun SpeciesCard(
    image: ImageWithMetadata?,
    name: String,
    speciesDescription: String,
    onMenuButtonClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onUnFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier,
    orderNum: String? = null,
    isFavorited: Boolean = false,
    scientificName: String? = null,
    imageContentDescription: String? = null,
    isRow: Boolean = true,
    cornerRadiusDp: Dp = 8.dp,
    contentScale: ContentScale = ContentScale.Crop,
    onClick: (() -> Unit)? = null,
    transitionKey: TransitionImageKey? = null
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
                image = image,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentDescription = imageContentDescription,
                shape = imageShape,
                contentScale = contentScale,
                orderNum = orderNum,
                isFavorited = isFavorited,
                onFavoriteClick = onFavoriteClick,
                onUnFavoriteClick = onUnFavoriteClick,
                transitionKey = transitionKey
            )

            GetContent(
                modifier = Modifier.weight(1.3f),
                name = name,
                scientificName = scientificName,
                speciesDescription = speciesDescription,
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
                image = image,
                modifier = Modifier
                    .heightIn(min = 200.dp)
                ,
                contentDescription = imageContentDescription,
                shape = imageShape,
                contentScale = contentScale,
                orderNum = orderNum,
                isFavorited = isFavorited,
                onFavoriteClick = onFavoriteClick,
                onUnFavoriteClick = onUnFavoriteClick,
                transitionKey = transitionKey
            )

            GetContent(
                modifier = Modifier,
                name = name,
                scientificName = scientificName,
                speciesDescription = speciesDescription,
                onMenuButtonClick = onMenuButtonClick
            )
        }
    }


}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun GetImage(
    image: ImageWithMetadata?,
    shape: Shape,
    contentDescription: String?,
    orderNum: String?,
    isFavorited: Boolean,
    onFavoriteClick: (() -> Unit),
    onUnFavoriteClick: (() -> Unit),
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    transitionKey: TransitionImageKey?
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(Color.Green.copy(alpha = 0.3f))
        ,
        contentAlignment = Alignment.BottomCenter
    ) {

        TransitionImage(
            image = image,
            modifier = Modifier
                .matchParentSize(),
            shape = shape,
            contentScale = contentScale,
            contentDescription = contentDescription,
            showErrorIcon = false,
            transitionKey = transitionKey,
            metadataIconAlignment = Alignment.BottomEnd
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(horizontal = 8.dp, vertical = 1.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
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


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun GetContent(
    modifier: Modifier = Modifier,
    name: String,
    speciesDescription: String,
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
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.sharedBoundsText(
                        textKey = TransitionTextKey(
                            text = name,
                            textType = TransitionTextType.Title
                        )
                    )
                )
                if (scientificName != null) {
                    Text(
                        text = scientificName,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.sharedBoundsText(
                            textKey = TransitionTextKey(
                                text = scientificName,
                                textType = TransitionTextType.Content
                            )
                        )
                    )
                }

            }
            IconButton(onClick = onMenuButtonClick) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
            }
        }
        Text(
            text = speciesDescription,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            overflow = TextOverflow.Ellipsis,
            maxLines = 10,
            modifier = Modifier.sharedBoundsText(
                textKey = TransitionTextKey(
                    text = speciesDescription,
                    textType = TransitionTextType.Description
                )
            )
        )
    }
}



@Preview(showBackground = true)
@Composable
fun SpeciesCardPreviewRow() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        SpeciesCard(
            species = SampleDatas.generateSpeciesDetail(),
            modifier = Modifier.fillMaxWidth(),
            orderNum = "10",
            onClick = {},
            onFavoriteClick = {},
            onMenuButtonClick = {},
            onUnFavoriteClick = {}
        )
    }
}


@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun SpeciesCardPreviewVertical() {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(200.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(2){
            SpeciesCard(
                species = SampleDatas.generateSpeciesDetail(),
                orderNum = "${it +1}",
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