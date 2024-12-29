package com.masterplus.animals.core.shared_features.ad.presentation.components

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.paging.compose.LazyPagingItems
import com.masterplus.animals.core.domain.models.Item
import com.masterplus.animals.core.extentions.hasLimitException


fun <T: Item> LazyListScope.ContinueWithAdButton(
    pagingItems: LazyPagingItems<T>,
    onWatchAd: () -> Unit
){
    if(pagingItems.hasLimitException()){
        item {
            TextButton(
                onClick = onWatchAd
            ) {
                Text("Devam etmek için reklam izleminiz gerekiyor")
            }
        }
    }
}