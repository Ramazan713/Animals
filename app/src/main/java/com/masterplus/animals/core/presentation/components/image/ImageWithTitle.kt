package com.masterplus.animals.core.presentation.components.image

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.extentions.sharedBoundsText
import com.masterplus.animals.core.presentation.components.OrderText
import com.masterplus.animals.core.presentation.models.ImageWithTitleModel
import com.masterplus.animals.core.presentation.utils.ColorUtils
import com.masterplus.animals.core.shared_features.theme.domain.enums.ThemeEnum
import com.masterplus.animals.core.shared_features.theme.domain.models.ThemeModel
import com.masterplus.animals.core.presentation.transition.TransitionImageKey
import com.masterplus.animals.core.presentation.transition.TransitionImageType
import com.masterplus.animals.ui.theme.AnimalsTheme


@Composable
fun ImageWithTitle(
    model: CategoryData,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    size: DpSize = DpSize(150.dp, 180.dp),
    shape: Shape = RoundedCornerShape(8.dp),
    contentScale: ContentScale = ContentScale.Crop,
    order: Int? = null,
    useTransition: Boolean = false
){
    ImageWithTitle(
        imageData = model.imageUrl ?: "",
        title = model.title,
        modifier = modifier,
        subTitle = model.secondaryTitle,
        onClick = onClick,
        contentDescription = model.title,
        size = size,
        shape = shape,
        contentScale = contentScale,
        order = order,
        useTransition = model.imageUrl != null && useTransition,
        transitionKey = TransitionImageType.fromCategoryType(model.categoryType)?.let {
            TransitionImageKey(
                id = model.id ?: 0,
                imageType = it
            )
        }
    )
}

@Composable
fun ImageWithTitle(
    model: ImageWithTitleModel,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    size: DpSize = DpSize(150.dp, 180.dp),
    shape: Shape = RoundedCornerShape(8.dp),
    contentScale: ContentScale = ContentScale.Crop,
    order: Int? = null
){
    ImageWithTitle(
        imageData = model.imageUrl ?: "",
        title = model.title,
        modifier = modifier,
        subTitle = model.subTitle,
        onClick = onClick,
        contentDescription = model.contentDescription,
        size = size,
        shape = shape,
        contentScale = contentScale,
        order = order,
        useTransition = model.imageUrl != null,
        transitionKey = null
    )
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ImageWithTitle(
    imageData: Any,
    title: String,
    modifier: Modifier = Modifier,
    subTitle: String? = null,
    onClick: (() -> Unit)? = null,
    contentDescription: String? = null,
    size: DpSize = DpSize(150.dp, 180.dp),
    shape: Shape = RoundedCornerShape(8.dp),
    contentScale: ContentScale = ContentScale.Crop,
    order: Int? = null,
    transitionKey: TransitionImageKey?,
    useTransition: Boolean = transitionKey != null,
) {
    Box(
        modifier = modifier
            .clip(shape)
            .size(size)
            .background(Color.White)
            .clickable(
                enabled = onClick != null,
            ) {
                onClick?.invoke()
            }
        ,
        contentAlignment = Alignment.BottomCenter
    ) {

        OrderText(
            order = order,
            modifier = Modifier
                .padding(8.dp)
                .zIndex(2f)
                .align(Alignment.TopStart)
        )

        GetTitleSection(
            title = title,
            subTitle = subTitle,
            height = minOf(size.height / 3, 100.dp),
            useTransition = useTransition
        )
        TransitionImage(
            imageData = imageData,
            shape = shape,
            transitionKey = transitionKey,
            enabled = useTransition,
            modifier = Modifier
                .matchParentSize()
            ,
            contentScale = contentScale,
            contentDescription = contentDescription,
        )

    }
}


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun GetTitleSection(
    title: String,
    subTitle: String?,
    height: Dp,
    useTransition: Boolean,
){
    val hasSubtitle = subTitle != null && subTitle.trim() != ""
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .background(
                Brush.verticalGradient(*ColorUtils.getImageGradientColors())
            )
            .padding(horizontal = 10.dp)
            .zIndex(1f),
        contentAlignment = Alignment.CenterStart
    ){
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
        ) {
            Spacer(modifier = Modifier.weight(if(hasSubtitle) 2f else 1f))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .sharedBoundsText(
                        enabled = useTransition,
                        contentStateKey = title
                    )
            )
            if(hasSubtitle){
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subTitle ?: "",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = Color.White
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .sharedBoundsText(
                            enabled = useTransition,
                            contentStateKey = subTitle ?: "",
                        )
                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ImageWithTitlePreview() {
    AnimalsTheme(
        state = ThemeModel(
            themeEnum = ThemeEnum.Dark
        )
    ) {
        Column {
            ImageWithTitle(
                title = "Kartallar",
                subTitle = "sadsadasdasdasd",
                imageData = "",
                transitionKey = TransitionImageKey(
                    id = 1,
                    TransitionImageType.Order
                )
            )
            ImageWithTitle(
                title = "Kartallar",
                imageData = "",
                transitionKey = TransitionImageKey(
                    id = 2,
                    TransitionImageType.Order
                )
            )
        }
    }
}