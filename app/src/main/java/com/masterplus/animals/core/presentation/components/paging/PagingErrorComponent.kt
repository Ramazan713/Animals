package com.masterplus.animals.core.presentation.components.paging

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdsClick
import androidx.compose.material.icons.filled.SignalWifiOff
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.masterplus.animals.core.data.mediators.RemoteMediatorError
import com.masterplus.animals.core.domain.models.Item
import com.masterplus.animals.core.extentions.getAnyExceptionOrNull
import com.masterplus.animals.core.extentions.getExceptionOrNull


@Composable
private fun PagingErrorComponent(
    error: RemoteMediatorError,
    onWatchAd: () -> Unit,
    modifier: Modifier = Modifier
) {
    when(error){
        RemoteMediatorError.NoInternetConnectionException -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                Icon(Icons.Default.SignalWifiOff, contentDescription = null)
                Text("İnternet bağlantınız bulunmamaktadır")
            }
        }
        RemoteMediatorError.ReadLimitExceededException -> {
            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onWatchAd
            ) {
                Icon(Icons.Default.AdsClick, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Devam etmek için reklam izleminiz gerekiyor")
            }
        }
    }
}

fun <T: Item> LazyListScope.AppendErrorHandlerComponent(
    pagingItems: LazyPagingItems<T>,
    onWatchAd: () -> Unit,
    modifier: Modifier = Modifier
){
    if(pagingItems.itemCount == 0) return
    val error = pagingItems.loadState.append.getExceptionOrNull() ?: pagingItems.loadState.refresh.getExceptionOrNull()
    if(error != null){
        item {
            PagingErrorComponent(
                error = error,
                modifier = modifier,
                onWatchAd = onWatchAd
            )
        }
    }
}

fun <T: Item> LazyListScope.PrependErrorHandlerComponent(
    pagingItems: LazyPagingItems<T>,
    onWatchAd: () -> Unit,
    modifier: Modifier = Modifier
){
    if(pagingItems.itemCount == 0) return
    pagingItems.loadState.prepend.getExceptionOrNull()?.let { error ->
        item {
            PagingErrorComponent(
                error = error,
                modifier = modifier,
                onWatchAd = onWatchAd
            )
        }
    }
}



fun <T: Item> LazyGridScope.AppendErrorHandlerComponent(
    pagingItems: LazyPagingItems<T>,
    onWatchAd: () -> Unit,
    modifier: Modifier = Modifier
){
    if(pagingItems.itemCount == 0) return
    val error = pagingItems.loadState.append.getExceptionOrNull() ?: pagingItems.loadState.refresh.getExceptionOrNull()
    if(error != null){
        item(
            span = { GridItemSpan(maxLineSpan) }
        ) {
            PagingErrorComponent(
                error = error,
                modifier = modifier,
                onWatchAd = onWatchAd
            )
        }
    }
}


fun <T: Item> LazyGridScope.PrependErrorHandlerComponent(
    pagingItems: LazyPagingItems<T>,
    onWatchAd: () -> Unit,
    modifier: Modifier = Modifier
){
    if(pagingItems.itemCount == 0) return
    pagingItems.loadState.prepend.getExceptionOrNull()?.let { error ->
        item(
            span = { GridItemSpan(maxLineSpan) }
        ) {
            PagingErrorComponent(
                error = error,
                modifier = modifier,
                onWatchAd = onWatchAd
            )
        }
    }
}




