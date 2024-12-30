package com.masterplus.animals.core.data.mediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.masterplus.animals.core.data.extensions.toRemoteLoadType
import com.masterplus.animals.core.domain.constants.K
import com.masterplus.animals.core.domain.enums.ContentType
import com.masterplus.animals.core.domain.enums.RemoteLoadType
import com.masterplus.animals.core.domain.enums.RemoteSourceType
import com.masterplus.animals.core.domain.models.Item
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.domain.utils.ReadLimitExceededException
import com.masterplus.animals.core.shared_features.analytics.domain.repo.ServerReadCounter
import com.masterplus.animals.core.shared_features.database.AppDatabase
import com.masterplus.animals.core.shared_features.database.entity.RemoteKeyEntity
import java.io.IOException


abstract class BaseRemoteMediator<T: Item>(
    override val db: AppDatabase,
    readCounter: ServerReadCounter,
    targetItemId: Int?
): BaseRemoteMediator2<T, T>(db, readCounter, targetItemId)

@OptIn(ExperimentalPagingApi::class)
abstract class BaseRemoteMediator2<T: Item, D: Item>(
    protected open val db: AppDatabase,
    private val readCounter: ServerReadCounter,
    private val targetItemId: Int?
): RemoteMediator<Int, T>() {

    abstract val saveRemoteKey: String
    abstract val contentType: ContentType

    abstract suspend fun fetchData(
        loadKey: Int?,
        loadType: RemoteLoadType,
        sourceType: RemoteSourceType,
        limit: Int
    ): DefaultResult<List<D>>

    abstract suspend fun insertData(items: List<D>)

    abstract suspend fun isItemExists(itemId: Int, label: String): Boolean

    open suspend fun clearTable(){}

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, T>): MediatorResult {
        return try {
            val remoteKey = db.withTransaction {
                db.remoteKeyDao.remoteKeyByQuery(saveRemoteKey)
            }
            val loadKey = when (loadType) {
                LoadType.REFRESH -> {
                    if(targetItemId != null){
                        val exists = isItemExists(targetItemId, saveRemoteKey)
                        if(exists) return MediatorResult.Success(endOfPaginationReached = false)
                        else targetItemId
                    }
                    else{
                        return MediatorResult.Success(endOfPaginationReached = false)
                    }
                }
                LoadType.PREPEND ->{
                    getRemoteKeyForFirstItem(state, remoteKey) ?: return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                }
                LoadType.APPEND -> {
                    val key = getRemoteKeyForLastItem(state, remoteKey)
                    if(remoteKey != null && key == null)return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                    key
                }
            }
            if(checkReadExceedLimit()){
                return MediatorResult.Error(ReadLimitExceededException)
            }
            val dataResponse = fetchData(
                loadKey = loadKey,
                loadType = loadType.toRemoteLoadType(),
                limit = state.config.pageSize,
                sourceType = RemoteSourceType.DEFAULT
            ).getSuccessData!!
            println("AppXXXX: mediator: loadType: $loadType::$loadKey::refresh: ${targetItemId} ::remoteKey: ${remoteKey} ")
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    clearTable()
                }
                val nextKey = getNextKey(dataResponse, loadType, remoteKey)
                val prevKey = getPrevKey(dataResponse, loadType, remoteKey)
                val updatedRemoteKey = RemoteKeyEntity(
                    label = saveRemoteKey,
                    nextKey = nextKey,
                    prevKey = prevKey
                )
                db.remoteKeyDao.insertOrReplace(updatedRemoteKey)
                insertData(dataResponse)
            }
            MediatorResult.Success(
                endOfPaginationReached = dataResponse.isEmpty()
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: Exception) {
            println("AppXXXX errorx: $e")
            MediatorResult.Error(e)
        }
    }

    open suspend fun checkReadExceedLimit(): Boolean{
        val counter = readCounter.getCounter(contentType)
        return counter > K.READ_EXCEED_LIMIT
    }

    open fun getNextKey(items: List<D>, loadType: LoadType,  remoteKey: RemoteKeyEntity?): String? {
        return when (loadType) {
            LoadType.REFRESH, LoadType.APPEND -> items.lastOrNull()?.id?.toString()
            else -> remoteKey?.nextKey
        }
    }

    open fun getPrevKey(items: List<D>, loadType: LoadType,  remoteKey: RemoteKeyEntity?): String? {
        return when (loadType) {
            LoadType.REFRESH, LoadType.PREPEND -> items.firstOrNull()?.id?.toString()
            else -> remoteKey?.prevKey
        }
    }

    private fun getRemoteKeyForFirstItem(state: PagingState<Int, T>, remoteKey: RemoteKeyEntity?): Int? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }
            ?.data?.firstOrNull()?.id ?: remoteKey?.nextKey?.toIntOrNull()
    }

    private fun getRemoteKeyForLastItem(state: PagingState<Int, T>, remoteKey: RemoteKeyEntity?): Int? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }
            ?.data?.lastOrNull()?.id ?: remoteKey?.nextKey?.toIntOrNull()
    }
}