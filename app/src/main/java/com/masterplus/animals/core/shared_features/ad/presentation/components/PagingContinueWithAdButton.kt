package com.masterplus.animals.core.shared_features.ad.presentation.components

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.masterplus.animals.core.domain.models.Item
import com.masterplus.animals.core.extentions.hasLimitException


fun <T: Item> LazyListScope.ContinueWithAdButton(
    pagingItems: LazyPagingItems<T>,
    onWatchAd: () -> Unit,
    modifier: Modifier = Modifier
){
    if(pagingItems.hasLimitException()){
        item {
            ContinueWithAdButton(
                modifier = modifier,
                onWatchAd = onWatchAd
            )
        }
    }
}

fun <T: Item> LazyGridScope.ContinueWithAdButton(
    pagingItems: LazyPagingItems<T>,
    onWatchAd: () -> Unit,
    modifier: Modifier = Modifier
){
    if(pagingItems.hasLimitException()){
        item(
            span = { GridItemSpan(maxLineSpan) }
        ) {
            ContinueWithAdButton(
                modifier = modifier,
                onWatchAd = onWatchAd
            )
        }
    }
}


@Composable
fun ContinueWithAdButton(
    onWatchAd: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        modifier = modifier,
        onClick = onWatchAd
    ) {
        Text("Devam etmek için reklam izleminiz gerekiyor")
    }
}
