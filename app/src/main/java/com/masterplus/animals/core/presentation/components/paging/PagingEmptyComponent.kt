package com.masterplus.animals.core.presentation.components.paging

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdsClick
import androidx.compose.material.icons.filled.SignalWifiOff
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.masterplus.animals.R
import com.masterplus.animals.core.data.mediators.RemoteMediatorError
import com.masterplus.animals.core.domain.models.Item
import com.masterplus.animals.core.extentions.getAnyExceptionOrNull

@Composable
fun <T: Item> PagingEmptyComponent(
    pagingItems: LazyPagingItems<T>,
    onWatchAd: () -> Unit,
    modifier: Modifier = Modifier
) {
    pagingItems.getAnyExceptionOrNull().let { error ->
        when(error){
            RemoteMediatorError.NoInternetConnectionException -> {
                Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.SignalWifiOff,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp)
                    )
                    Text(
                        "İnternet bağlantınız bulunmamaktadır",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            RemoteMediatorError.ReadLimitExceededException -> {
                Column(
                    modifier = modifier,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.AdsClick,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp)
                    )
                    Text(
                        "Devam etmek için reklam izleminiz gerekiyor",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
                    FilledTonalButton(
                        onClick = onWatchAd
                    ) {
                        Text("Reklam izle")
                    }
                }
            }
            null -> {
                Text(
                    modifier = modifier,
                    text = stringResource(id = R.string.not_fount_any_result),
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}