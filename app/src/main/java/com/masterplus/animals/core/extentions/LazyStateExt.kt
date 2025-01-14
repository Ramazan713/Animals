package com.masterplus.animals.core.extentions

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridLayoutInfo
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridLayoutInfo
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.paging.compose.LazyPagingItems
import com.masterplus.animals.core.domain.models.ItemOrder


@Composable
fun LazyListState.visibleMiddlePosition(): Int{
    return remember {
        derivedStateOf {
            (firstVisibleItemIndex + (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0)) / 2
        }
    }.value
}

@Composable
fun LazyGridState.visibleMiddlePosition(): Int{
    return remember {
        derivedStateOf {
            (firstVisibleItemIndex + (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0)) / 2
        }
    }.value
}

@Composable
fun LazyStaggeredGridState.visibleMiddlePosition(): Int{
    return remember {
        derivedStateOf {
            (firstVisibleItemIndex + (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0)) / 2
        }
    }.value
}

@Composable
fun <T: ItemOrder>LazyPagingItems<T>.visibleMiddleItemOrderKey(pos: Int): Int?{
    return remember(pos) {
        derivedStateOf {
            if(pos >= itemCount) null else peek(pos)?.orderKey
        }
    }.value
}

@Composable
fun <T: ItemOrder>LazyPagingItems<T>.visibleMiddleItemOrderKey(lazyListState: LazyListState): Int?{
    val visibleMiddlePos = lazyListState.visibleMiddlePosition()
    return visibleMiddleItemOrderKey(visibleMiddlePos)
}

@Composable
fun <T: ItemOrder>LazyPagingItems<T>.visibleMiddleItemOrderKey(lazyGridState: LazyGridState): Int?{
    val visibleMiddlePos = lazyGridState.visibleMiddlePosition()
    return visibleMiddleItemOrderKey(visibleMiddlePos)
}


@Composable
fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}


@Composable
fun LazyGridState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

@Composable
fun LazyStaggeredGridState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) { mutableIntStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by remember(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }
    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}


fun LazyGridLayoutInfo.isNumberInRange(number: Int): Boolean?{
    visibleItemsInfo.let { info->
        val first = info.firstOrNull()?.index
        val last = info.lastOrNull()?.index
        if(first == null || last == null) return null
        return number in first..last
    }
}



fun LazyStaggeredGridLayoutInfo.isNumberInRange(number: Int): Boolean?{
    visibleItemsInfo.let { info->
        val first = info.firstOrNull()?.index
        val last = info.lastOrNull()?.index
        if(first == null || last == null) return null
        return number in first..last
    }
}
