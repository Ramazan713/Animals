package com.masterplus.animals.core.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageScope
import coil.request.ImageRequest
import com.masterplus.animals.R

@Composable
fun DefaultImage(
    imageData: Any,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale? = ContentScale.Crop,
    error: @Composable() (SubcomposeAsyncImageScope.(AsyncImagePainter.State.Error) -> Unit)? = null,
    @DrawableRes errorResource: Int? = null
) {
    val context = LocalContext.current
    val currentContentScale = contentScale ?: ContentScale.Crop
    SubcomposeAsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(context)
            .data(imageData)
            .build(),
        contentScale = currentContentScale,
        contentDescription = contentDescription,
        loading = {
            Box(
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        },
        error = {
            error ?: GetErrorHolder(contentScale = currentContentScale, errorResource = errorResource)
        },
    )
}


@Composable
private fun GetErrorHolder(
    contentScale: ContentScale,
    @DrawableRes errorResource: Int?
){
    Box(
        contentAlignment = Alignment.TopEnd
    ) {
        Image(
            modifier = Modifier,
            contentScale = contentScale,
            painter = painterResource(id = errorResource ?: R.drawable.all_animals),
            contentDescription = null
        )
        Icon(
            modifier = Modifier
                .padding(12.dp)
                .zIndex(1f),
            painter = painterResource(
                id = R.drawable.baseline_error_24,
            ),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        )
    }
}
