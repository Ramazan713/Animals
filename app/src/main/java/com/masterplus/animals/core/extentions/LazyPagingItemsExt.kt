package com.masterplus.animals.core.extentions

import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.masterplus.animals.core.data.mediators.RemoteMediatorError


fun LoadState.getExceptionOrNull(): RemoteMediatorError?{
    if (this is LoadState.Error && this.error is RemoteMediatorError){
        return this.error as RemoteMediatorError
    }
    return null
}

fun <T: Any> LazyPagingItems<T>.getAnyExceptionOrNull(): RemoteMediatorError?{
    return listOf(loadState.append, loadState.prepend, loadState.refresh)
        .map { it.getExceptionOrNull() }
        .firstNotNullOfOrNull { it }
}

fun <T: Any> LazyPagingItems<T>.isEmptyResult(): Boolean{
    return itemCount == 0 && !isAnyItemLoading()
}



fun <T: Any> LazyPagingItems<T>.isAppendItemLoading(): Boolean{
    val isLocalAppend = loadState.source.append is LoadState.Loading && itemCount != 0
    val isAppend = loadState.append is LoadState.Loading && itemCount != 0
    return isLocalAppend || isAppend
}

fun <T: Any> LazyPagingItems<T>.isPrependItemLoading(): Boolean{
    val isLocalPrepend = loadState.source.prepend is LoadState.Loading
    val isPrepend = loadState.prepend is LoadState.Loading
    return isLocalPrepend || isPrepend
}

fun <T: Any> LazyPagingItems<T>.isLoading(): Boolean{
    val isLocalRefresh = loadState.source.refresh is LoadState.Loading
    val isRefresh = loadState.refresh is LoadState.Loading
    val isFirstAppendLoading = loadState.append is LoadState.Loading && itemCount == 0
    return isLocalRefresh || isRefresh || isFirstAppendLoading
}

fun <T: Any> LazyPagingItems<T>.isAnyItemLoading(): Boolean{
    return isAppendItemLoading() || isPrependItemLoading()
}