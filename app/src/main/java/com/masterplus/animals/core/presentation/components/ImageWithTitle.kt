package com.masterplus.animals.core.presentation.components

import androidx.compose.foundation.Image
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.masterplus.animals.R
import com.masterplus.animals.core.presentation.models.ImageWithTitleModel
import com.masterplus.animals.core.presentation.utils.ColorUtils


@Composable
fun ImageWithTitle(
    model: ImageWithTitleModel,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    size: DpSize = DpSize(150.dp, 180.dp),
    shape: Shape = RoundedCornerShape(8.dp),
    contentScale: ContentScale = ContentScale.Crop,
){
    ImageWithTitle(
        imageData = model.imageUrl,
        title = model.title,
        modifier = modifier,
        subTitle = model.subTitle,
        onClick = onClick,
        contentDescription = model.contentDescription,
        size = size,
        shape = shape,
        contentScale = contentScale,
    )
}


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
) {
    val context = LocalContext.current

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

        GetTitleSection(
            title = title,
            subTitle = subTitle
        )

        DefaultImage(
            imageData = imageData,
            modifier = Modifier
                .matchParentSize(),
            contentScale = contentScale,
            contentDescription = contentDescription,
        )
    }
}


@Composable
private fun GetTitleSection(
    title: String,
    subTitle: String?
){
    val hasSubtitle = subTitle != null && subTitle.trim() != ""
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
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
                    color = MaterialTheme.colorScheme.inverseOnSurface
                )
            )
            if(hasSubtitle){
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subTitle ?: "",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.inverseOnSurface
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
    Column {
        ImageWithTitle(
            title = "Kartallar",
            subTitle = "sadsadasdasdasd",
            imageData = ""
        )
        ImageWithTitle(
            title = "Kartallar",
            imageData = ""
        )
    }


}