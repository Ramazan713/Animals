package com.masterplus.animals.core.presentation.components.image

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageScope
import coil3.request.ImageRequest
import com.masterplus.animals.R
import com.masterplus.animals.core.presentation.components.DefaultToolTip
import com.masterplus.animals.core.presentation.components.loading.SharedCircularProgress
import com.masterplus.animals.core.shared_features.theme.domain.enums.ThemeEnum
import com.masterplus.animals.core.shared_features.theme.domain.models.ThemeModel
import com.masterplus.animals.ui.theme.AnimalsTheme
import com.masterplus.animals.ui.theme.lightColorScheme

@Composable
fun DefaultImage(
    imageData: Any,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale? = null,
    error: @Composable() (SubcomposeAsyncImageScope.(AsyncImagePainter.State.Error) -> Unit)? = null,
    success: @Composable (SubcomposeAsyncImageScope.(AsyncImagePainter.State.Success) -> Unit)? = null,
    onSuccess: ((AsyncImagePainter. State. Success) -> Unit)? = null,
    @DrawableRes errorImageResource: Int? = null,
    cacheKey: String? = null,
    showErrorIcon: Boolean = true,
    showLoadingBackgroundColor: Boolean = true
) {
    val context = LocalContext.current
    val currentContentScale = contentScale ?: ContentScale.Crop
    SubcomposeAsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(context)
            .data(imageData)
            .placeholderMemoryCacheKey(cacheKey)
            .memoryCacheKey(cacheKey)
            .build(),
        contentScale = currentContentScale,
        contentDescription = contentDescription,
        onSuccess = onSuccess,
        success = success,
        loading = {
            SharedCircularProgress(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(
                        alpha = if(showLoadingBackgroundColor) 1f else 0.000001f)
                    )
            )
        },
        error = {
            error ?: GetErrorHolder(
                contentScale = currentContentScale,
                errorResource = errorImageResource,
                showErrorIcon = showErrorIcon,
                modifier = Modifier.fillMaxWidth()
            )
        },
    )
}


@Composable
private fun GetErrorHolder(
    contentScale: ContentScale,
    @DrawableRes errorResource: Int?,
    showErrorIcon: Boolean,
    modifier: Modifier = Modifier
){
    val errorText = stringResource(R.string.something_went_wrong)

    var showErrorDia by rememberSaveable {
        mutableStateOf(false)
    }

    Box(
        contentAlignment = Alignment.TopEnd
    ) {
        Image(
            modifier = modifier,
            contentScale = contentScale,
            painter = painterResource(id = errorResource ?: R.drawable.animals_plants),
            contentDescription = null
        )
        if(showErrorIcon){
            DefaultToolTip(tooltip = errorText) {
                IconButton(
                    onClick = { showErrorDia = true },
                    modifier = Modifier
                        .zIndex(1f),
                ) {
                    ErrorIcon()
                }
            }
        }
    }

    if(showErrorDia){
        ShowErrorDia(
            errorText = errorText,
            onDismiss = { showErrorDia = false },
        )
    }
}


@Composable
private fun ShowErrorDia(
    errorText: String,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Ok")
            }
        },
        title = {
            Text(text = errorText)
        },
        icon = {
            ErrorIcon()
        }
    )
}

@Composable
private fun ErrorIcon(
    modifier: Modifier = Modifier
) {
    Icon(
        modifier = modifier,
        painter = painterResource(
            id = R.drawable.baseline_error_24,
        ),
        contentDescription = stringResource(R.string.something_went_wrong),
        tint = MaterialTheme.lightColorScheme.error
    )
}


@Preview(showBackground = true)
@Composable
private fun DefaultImagePreview(modifier: Modifier = Modifier) {
   AnimalsTheme(
       state = ThemeModel(themeEnum = ThemeEnum.Dark)
   ) {
       DefaultImage(
           imageData = R.drawable.animals_plants,
//        imageData = "dads",
           modifier = Modifier
               .size(150.dp)
               .clip(RoundedCornerShape(8.dp))
               ,
       )
   }
}