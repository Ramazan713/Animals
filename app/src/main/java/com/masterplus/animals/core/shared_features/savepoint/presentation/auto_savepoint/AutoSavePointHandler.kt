package com.masterplus.animals.core.shared_features.savepoint.presentation.auto_savepoint

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.paging.compose.LazyPagingItems
import com.masterplus.animals.core.data.mediators.RemoteMediatorError
import com.masterplus.animals.core.domain.constants.K
import com.masterplus.animals.core.domain.models.ItemOrder
import com.masterplus.animals.core.extentions.getAnyExceptionOrNull
import com.masterplus.animals.core.extentions.visibleMiddleItemOrderKey
import com.masterplus.animals.core.presentation.dialogs.LoadingDialog
import com.masterplus.animals.core.presentation.utils.EventHandler
import com.masterplus.animals.core.presentation.utils.ListenEventLifecycle
import com.masterplus.animals.core.shared_features.ad.presentation.AdAction
import com.masterplus.animals.core.shared_features.ad.presentation.AdMobResultHandler
import com.masterplus.animals.core.shared_features.ad.presentation.AdState
import com.masterplus.animals.core.shared_features.ad.presentation.AdUiResult
import com.masterplus.animals.core.shared_features.ad.presentation.dialogs.ShowAdRequiredDia
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.ceil

typealias OrderKey = Int

@Composable
fun <T: ItemOrder> AutoSavePointHandler(
    state: AutoSavePointState,
    onAction: (AutoSavePointAction) -> Unit,
    onDestination: () -> SavePointDestination,
    contentType: SavePointContentType,
    onItemOrderKey: () -> Int?,
    itemInitPos: Int = 0,
    pagingItems: LazyPagingItems<T>,
    onInitPosResponse: ((Int) -> Unit)? = null,
    onLoadRequiredPage: (OrderKey) -> Unit,
    adState: AdState,
    onAdAction: (AdAction) -> Unit,
) {
    val currentOnInitPosResponse by rememberUpdatedState(onInitPosResponse)
    val currentOnDestination by rememberUpdatedState(onDestination)
    val scope = rememberCoroutineScope()

    var requiredPageRetry by rememberSaveable {
        mutableIntStateOf(0)
    }

    EventHandler(state.uiEvent) { uiEvent ->
        onAction(AutoSavePointAction.ClearUiEvent)
        when(uiEvent){
            is AutoSavePointEvent.LoadItemPos -> {
                val pos = uiEvent.pos
                requiredPageRetry = 0
                if(pos != null) {
                    currentOnInitPosResponse?.invoke(pos)
                    return@EventHandler
                }
            }
            AutoSavePointEvent.RetryPaging -> {
                pagingItems.retry()
            }
            is AutoSavePointEvent.ShowAd -> {
                onAdAction(AdAction.RequestShowRewardAd(K.AUTO_SAVEPOINT_AD_LABEL))
            }
            is AutoSavePointEvent.LoadRequiredPage -> {
                val error = pagingItems.getAnyExceptionOrNull()
                if(error != null){
                    val orderKeyExistsInList = pagingItems.itemSnapshotList.items.any { it.orderKey == uiEvent.orderKey }
                    if(orderKeyExistsInList) return@EventHandler
                    if(pagingItems.itemCount == 0) return@EventHandler
                    if(error is RemoteMediatorError.ReadLimitExceededException){
                        onAction(AutoSavePointAction.ShowDialog(AutoSavePointDialogEvent.ShowAdRequired))
                    }
                    return@EventHandler
                }
                onLoadRequiredPage(uiEvent.orderKey)
                pagingItems.refresh()
                scope.launch {
                    delay(1000)
                    requiredPageRetry += 1
                    if(requiredPageRetry > 3){
                        requiredPageRetry = 0
                        return@launch
                    }
                    onAction(AutoSavePointAction.RequestNavigateToPosByOrderKey(uiEvent.orderKey))
                }
            }
        }
    }

    AdMobResultHandler(
        adUiResult = adState.uiResult,
        onAdAction = onAdAction,
        label = K.AUTO_SAVEPOINT_AD_LABEL,
        onSafeAdResult = { result ->
            when(result){
                is AdUiResult.OnShowingRewardSuccess -> {
                    onAction(AutoSavePointAction.SuccessShowAd(
                        contentType = contentType.toContentType(),
                    ))
                }
                else -> Unit
            }
        }
    )

    if(adState.loadingRewardAd.isLoading && adState.loadingRewardAd.label == K.AUTO_SAVEPOINT_AD_LABEL){
        LoadingDialog()
    }

    LaunchedEffect(itemInitPos, contentType) {
        onAction(AutoSavePointAction.LoadSavePoint(
            initOrderKey = itemInitPos,
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
            onItemOrderKey()?.let { orderKey ->
                onAction(AutoSavePointAction.UpsertSavePoint(
                    destination = currentOnDestination(),
                    contentType = contentType,
                    orderKey = orderKey
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
                ShowAdRequiredDia(
                    onDismiss = close,
                    onApproved = {
                        onAdAction(AdAction.RequestShowRewardAd(K.AUTO_SAVEPOINT_AD_LABEL))
                    },
                    title = "Kayıt yükleme limitine ulaştınız"
                )
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T: ItemOrder> AutoSavePointHandler(
    state: AutoSavePointState,
    onAction: (AutoSavePointAction) -> Unit,
    onDestination: () -> SavePointDestination,
    contentType: SavePointContentType,
    onItemOrderKey: () -> Int?,
    topBarScrollBehaviour: TopAppBarScrollBehavior?,
    scrollToPos: suspend (Int) -> Unit,
    onLoadRequiredPage: (OrderKey) -> Unit,
    itemInitPos: Int = 0,
    pagingItems: LazyPagingItems<T>,
    adState: AdState,
    onAdAction: (AdAction) -> Unit,
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
        onItemOrderKey = onItemOrderKey,
        state = state,
        itemInitPos = itemInitPos,
        pagingItems = pagingItems,
        onAdAction = onAdAction,
        onLoadRequiredPage = onLoadRequiredPage,
        adState = adState,
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
fun <T: ItemOrder> AutoSavePointHandler(
    state: AutoSavePointState,
    onAction: (AutoSavePointAction) -> Unit,
    onDestination: () -> SavePointDestination,
    contentType: SavePointContentType,
    adState: AdState,
    onAdAction: (AdAction) -> Unit,
    onLoadRequiredPage: (OrderKey) -> Unit,
    itemInitPos: Int = 0,
    lazyListState: LazyListState,
    pagingItems: LazyPagingItems<T>,
    topBarScrollBehaviour: TopAppBarScrollBehavior? = null,
){
    val itemOrderKey = pagingItems.visibleMiddleItemOrderKey(lazyListState)
    AutoSavePointHandler(
        contentType = contentType,
        onDestination = onDestination,
        onAction = onAction,
        onItemOrderKey = { itemOrderKey },
        state = state,
        itemInitPos = itemInitPos,
        topBarScrollBehaviour = topBarScrollBehaviour,
        pagingItems = pagingItems,
        onAdAction = onAdAction,
        onLoadRequiredPage = onLoadRequiredPage,
        adState = adState,
        scrollToPos = { pos ->
            lazyListState.scrollToItem(pos)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T: ItemOrder> AutoSavePointHandler(
    state: AutoSavePointState,
    onAction: (AutoSavePointAction) -> Unit,
    onDestination: () -> SavePointDestination,
    contentType: SavePointContentType,
    adState: AdState,
    onAdAction: (AdAction) -> Unit,
    onLoadRequiredPage: (OrderKey) -> Unit,
    itemInitPos: Int = 0,
    lazyListState: LazyGridState,
    pagingItems: LazyPagingItems<T>,
    topBarScrollBehaviour: TopAppBarScrollBehavior? = null
){
    val itemPosId = pagingItems.visibleMiddleItemOrderKey(lazyListState)
    AutoSavePointHandler(
        contentType = contentType,
        onDestination = onDestination,
        onAction = onAction,
        onItemOrderKey = { itemPosId },
        state = state,
        itemInitPos = itemInitPos,
        pagingItems = pagingItems,
        topBarScrollBehaviour = topBarScrollBehaviour,
        onAdAction = onAdAction,
        adState = adState,
        onLoadRequiredPage = onLoadRequiredPage,
        scrollToPos = { pos ->
            lazyListState.scrollToItem(pos)
        }
    )
}