package com.masterplus.animals.test_utils

import androidx.paging.PagingSource
import androidx.paging.PagingState

class FakePagingSource<T : Any>(private val data: List<T>) : PagingSource<Int, T>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val position = params.key ?: 0
        val loadData = data.drop(position).take(params.loadSize)
        return LoadResult.Page(
            data = loadData,
            prevKey = if (position == 0) null else position - params.loadSize,
            nextKey = if (loadData.isEmpty()) null else position + params.loadSize
        )
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? = null
}