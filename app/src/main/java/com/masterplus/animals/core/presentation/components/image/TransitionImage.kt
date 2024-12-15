package com.masterplus.animals.core.presentation.components.image

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImageScope
import com.masterplus.animals.core.domain.models.ImageWithMetadata
import com.masterplus.animals.core.presentation.transition.LocalNavAnimatedVisibilityScope
import com.masterplus.animals.core.presentation.transition.LocalSharedTransitionScope
import com.masterplus.animals.core.presentation.transition.TransitionImageKey

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun TransitionImage(
    image: ImageWithMetadata?,
    transitionKey: TransitionImageKey?,
    modifier: Modifier = Modifier,
    shape: Shape? = null,
    contentDescription: String? = null,
    contentScale: ContentScale? = null,
    error: @Composable() (SubcomposeAsyncImageScope.(AsyncImagePainter.State.Error) -> Unit)? = null,
    @DrawableRes errorImageResource: Int? = null,
    sharedAnimatedVisibilityScope: AnimatedVisibilityScope? = null,
    sharedTransitionScope: SharedTransitionScope? = null,
    showErrorIcon: Boolean = true,
    enabled: Boolean = transitionKey != null,
    fallbackImageData: Any? = null,
) {
    val currentAnimatedScope = sharedAnimatedVisibilityScope ?: LocalNavAnimatedVisibilityScope.current

    with(sharedTransitionScope ?: LocalSharedTransitionScope.current){
        val cacheKey = transitionKey?.toString()
        DefaultImageMetadata(
            image = image,
            fallbackImageData = fallbackImageData,
            modifier = modifier
                .then(
                    if(transitionKey != null && this != null && currentAnimatedScope != null && enabled) Modifier
                        .sharedElement(
                            rememberSharedContentState(cacheKey ?: ""),
                            animatedVisibilityScope = currentAnimatedScope,
                        )
                    else Modifier
                )
                .then(
                    if(shape != null) Modifier.clip(shape)
                    else Modifier
                )
            ,
            contentDescription = contentDescription,
            contentScale = contentScale,
            error = error,
            errorImageResource = errorImageResource,
            showErrorIcon = showErrorIcon,
            cacheKey = cacheKey
        )
    }


}