package com.masterplus.animals.core.shared_features.savepoint.presentation.auto_savepoint

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import com.masterplus.animals.core.extentions.visibleMiddlePosition
import com.masterplus.animals.core.presentation.utils.EventHandler
import com.masterplus.animals.core.presentation.utils.ListenEventLifecycle
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import kotlinx.coroutines.launch

@Composable
fun AutoSavePointHandler(
    state: AutoSavePointState,
    onAction: (AutoSavePointAction) -> Unit,
    onDestination: () -> SavePointDestination,
    contentType: SavePointContentType,
    itemPosIndex: Int,
    itemInitPos: Int = 0,
    onInitPosResponse: ((Int) -> Unit)? = null
) {
    val currentOnInitPosResponse by rememberUpdatedState(onInitPosResponse)
    val currentOnDestination by rememberUpdatedState(onDestination)

    EventHandler(state.uiEvent) { uiEvent ->
        onAction(AutoSavePointAction.ClearUiEvent)
        when(uiEvent){
            is AutoSavePointEvent.LoadItemPos -> {
                currentOnInitPosResponse?.invoke(uiEvent.pos)
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

    ListenEventLifecycle(
        keys = arrayOf(contentType),
        onStop = {
            onAction(AutoSavePointAction.UpsertSavePoint(
                destination = currentOnDestination(),
                contentType = contentType,
                itemPosIndex = itemPosIndex
            ))
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoSavePointHandler(
    state: AutoSavePointState,
    onAction: (AutoSavePointAction) -> Unit,
    onDestination: () -> SavePointDestination,
    contentType: SavePointContentType,
    itemInitPos: Int = 0,
    lazyListState: LazyListState,
    topBarScrollBehaviour: TopAppBarScrollBehavior? = null
){
    val scope = rememberCoroutineScope()
    val itemPosIndex = lazyListState.visibleMiddlePosition()

    val animatedHeightOffset by animateFloatAsState(
        targetValue = topBarScrollBehaviour?.state?.heightOffsetLimit ?: 0f,
        animationSpec = tween(durationMillis = 700),
        label = "label"
    )

    AutoSavePointHandler(
        contentType = contentType,
        onDestination = onDestination,
        onAction = onAction,
        itemPosIndex = itemPosIndex,
        state = state,
        itemInitPos = itemInitPos,
        onInitPosResponse = {pos ->
            scope.launch {
                if(pos < 2) return@launch
                topBarScrollBehaviour?.state?.let { state->
                    state.heightOffset = animatedHeightOffset
                }
                lazyListState.scrollToItem(pos)
            }
        }
    )
}