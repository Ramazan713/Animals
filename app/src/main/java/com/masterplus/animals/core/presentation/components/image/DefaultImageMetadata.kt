package com.masterplus.animals.core.presentation.components.image

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImageContent
import coil.compose.SubcomposeAsyncImageScope
import com.masterplus.animals.core.domain.models.ImageWithMetadata
import com.masterplus.animals.core.presentation.dialogs.ShowImageMetadataDia
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.core.shared_features.theme.domain.enums.ThemeEnum
import com.masterplus.animals.core.shared_features.theme.domain.models.ThemeModel
import com.masterplus.animals.ui.theme.AnimalsTheme

@Composable
fun DefaultImageMetadata(
    image: ImageWithMetadata?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale? = null,
    error: @Composable() (SubcomposeAsyncImageScope.(AsyncImagePainter.State.Error) -> Unit)? = null,
    @DrawableRes errorImageResource: Int? = null,
    cacheKey: String? = null,
    showErrorIcon: Boolean = true,
    fallbackImageData: Any? = null,
    metadataIconAlignment: Alignment = Alignment.TopEnd
) {
    DefaultImage(
        imageData = image?.imageUrl ?: fallbackImageData ?:"",
        modifier = modifier,
        contentDescription = contentDescription,
        contentScale = contentScale,
        error = error,
        errorImageResource = errorImageResource,
        cacheKey = cacheKey,
        showErrorIcon = showErrorIcon,
        success = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                with(this@DefaultImage){
                    SubcomposeAsyncImageContent(
                        modifier = Modifier.matchParentSize()
                    )
                }
                image?.metadata?.let { metadata ->
                    var showInfoDia by rememberSaveable {
                        mutableStateOf(false)
                    }
                    IconButton(
                        onClick = { showInfoDia = true },
                        modifier = Modifier
                            .align(metadataIconAlignment)
                            .zIndex(1f),
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null)
                    }

                    if(showInfoDia){
                        ShowImageMetadataDia(
                            metadata = metadata,
                            imageData = image.imageUrl,
                            onDismiss = { showInfoDia = false },
                        )
                    }
                }
            }
        }
    )
}



@Preview(showBackground = true)
@Composable
private fun DefaultImageMetadataPreview() {
    AnimalsTheme(
        state = ThemeModel(themeEnum = ThemeEnum.Dark)
    ) {
        DefaultImageMetadata(
//            imageData = R.drawable.google_icon,
            image = SampleDatas.imageWithMetadata,


//        imageData = "dads",
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.Blue.copy(alpha = 0.3f)),
        )
    }
}