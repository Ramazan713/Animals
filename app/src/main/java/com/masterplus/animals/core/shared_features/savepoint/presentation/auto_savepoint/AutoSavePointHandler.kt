package com.masterplus.animals.core.shared_features.savepoint.presentation.auto_savepoint

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.LazyPagingItems
import com.masterplus.animals.R
import com.masterplus.animals.core.data.mediators.RemoteMediatorError
import com.masterplus.animals.core.domain.models.Item
import com.masterplus.animals.core.extentions.getAnyExceptionOrNull
import com.masterplus.animals.core.extentions.visibleMiddleItemId
import com.masterplus.animals.core.presentation.utils.EventHandler
import com.masterplus.animals.core.presentation.utils.ListenEventLifecycle
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import kotlinx.coroutines.launch
import kotlin.math.ceil


@Composable
fun <T: Item> AutoSavePointHandler(
    state: AutoSavePointState,
    onAction: (AutoSavePointAction) -> Unit,
    onDestination: () -> SavePointDestination,
    contentType: SavePointContentType,
    onItemPosId: () -> Int?,
    itemInitPos: Int = 0,
    pagingItems: LazyPagingItems<T>,
    onInitPosResponse: ((Int) -> Unit)? = null
) {
    val currentOnInitPosResponse by rememberUpdatedState(onInitPosResponse)
    val currentOnDestination by rememberUpdatedState(onDestination)

    EventHandler(state.uiEvent) { uiEvent ->
        onAction(AutoSavePointAction.ClearUiEvent)
        when(uiEvent){
            is AutoSavePointEvent.LoadItemPos -> {
                val pos = uiEvent.pos
                if(pos != null) {
                    currentOnInitPosResponse?.invoke(pos)
                    return@EventHandler
                }
                val error = pagingItems.getAnyExceptionOrNull()
                if(error != null){
                    if(error is RemoteMediatorError.ReadLimitExceededException){
                        onAction(AutoSavePointAction.ShowDialog(AutoSavePointDialogEvent.ShowAdRequired))
                    }
                }else{
                    pagingItems.refresh()
                }
            }
            AutoSavePointEvent.RetryPaging -> {
                pagingItems.retry()
            }
            AutoSavePointEvent.ShowAd -> {
                onAction(AutoSavePointAction.SuccessShowAd)
            }
        }
    }

    LaunchedEffect(itemInitPos, contentType) {
        onAction(AutoSavePointAction.LoadSavePoint(
            initItemPos = itemInitPos,
            contentType = contentType,
            destination = currentOnDestination()
        ))
    }

    LaunchedEffect(contentType,currentOnDestination){
        onAction(AutoSavePointAction.Init(
            contentType = contentType,
            destination = currentOnDestination()
        ))
    }

    ListenEventLifecycle(
        keys = arrayOf(contentType),
        onStop = {
            onItemPosId()?.let { itemId ->
                onAction(AutoSavePointAction.UpsertSavePoint(
                    destination = currentOnDestination(),
                    contentType = contentType,
                    itemId = itemId
                ))
            }
        }
    )

    state.dialogEvent?.let { dialogEvent ->
        val close = remember { {
            onAction(AutoSavePointAction.ShowDialog(null))
        } }
        when(dialogEvent){
            is AutoSavePointDialogEvent.ShowAdRequired -> {
                AlertDialog(
                    onDismissRequest = close,
                    title = {
                        Text("Kayıt yükleme limitine ulaştınız")
                    },
                    text = {
                        Text("Devam etmek için reklam izlemeniz gerekmektedir")
                    },
                    dismissButton = {
                        TextButton(
                            onClick = close,
                        ){
                            Text(stringResource(R.string.cancel))
                        }
                    },
                    confirmButton = {
                        FilledTonalButton(
                            onClick = {
                                onAction(AutoSavePointAction.ShowAd)
                                close()
                            }
                        ) {
                            Text("Onayla")
                        }
                    }
                )
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T: Item> AutoSavePointHandler(
    state: AutoSavePointState,
    onAction: (AutoSavePointAction) -> Unit,
    onDestination: () -> SavePointDestination,
    contentType: SavePointContentType,
    onItemPosId: () -> Int?,
    topBarScrollBehaviour: TopAppBarScrollBehavior?,
    scrollToPos: suspend (Int) -> Unit,
    itemInitPos: Int = 0,
    pagingItems: LazyPagingItems<T>,
){
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val animatedHeightOffset by animateFloatAsState(
        targetValue = topBarScrollBehaviour?.state?.heightOffsetLimit ?: 0f,
        animationSpec = tween(durationMillis = 700),
        label = "animatedHeightOffset"
    )

    AutoSavePointHandler(
        contentType = contentType,
        onDestination = onDestination,
        onAction = onAction,
        onItemPosId = onItemPosId,
        state = state,
        itemInitPos = itemInitPos,
        pagingItems = pagingItems,
        onInitPosResponse = {pos ->
            scope.launch {
                val itemSize = ceil(configuration.screenHeightDp / 180f).toInt() - 2
                if(pos < itemSize) return@launch
                topBarScrollBehaviour?.state?.let { state->
                    state.heightOffset = animatedHeightOffset
                }
                scrollToPos(pos)
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T: Item> AutoSavePointHandler(
    state: AutoSavePointState,
    onAction: (AutoSavePointAction) -> Unit,
    onDestination: () -> SavePointDestination,
    contentType: SavePointContentType,
    itemInitPos: Int = 0,
    lazyListState: LazyListState,
    pagingItems: LazyPagingItems<T>,
    topBarScrollBehaviour: TopAppBarScrollBehavior? = null
){
    val itemPosId = pagingItems.visibleMiddleItemId(lazyListState)
    AutoSavePointHandler(
        contentType = contentType,
        onDestination = onDestination,
        onAction = onAction,
        onItemPosId = { itemPosId },
        state = state,
        itemInitPos = itemInitPos,
        topBarScrollBehaviour = topBarScrollBehaviour,
        pagingItems = pagingItems,
        scrollToPos = { pos ->
            println("AppXXXX: pos: $pos")
            lazyListState.scrollToItem(pos)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T: Item> AutoSavePointHandler(
    state: AutoSavePointState,
    onAction: (AutoSavePointAction) -> Unit,
    onDestination: () -> SavePointDestination,
    contentType: SavePointContentType,
    itemInitPos: Int = 0,
    lazyListState: LazyGridState,
    pagingItems: LazyPagingItems<T>,
    topBarScrollBehaviour: TopAppBarScrollBehavior? = null
){
    val itemPosId = pagingItems.visibleMiddleItemId(lazyListState)
    AutoSavePointHandler(
        contentType = contentType,
        onDestination = onDestination,
        onAction = onAction,
        onItemPosId = { itemPosId },
        state = state,
        itemInitPos = itemInitPos,
        pagingItems = pagingItems,
        topBarScrollBehaviour = topBarScrollBehaviour,
        scrollToPos = { pos ->
            lazyListState.scrollToItem(pos)
        }
    )
}