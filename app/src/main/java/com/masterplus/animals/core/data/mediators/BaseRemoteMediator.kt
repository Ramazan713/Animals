package com.masterplus.animals.core.data.mediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.masterplus.animals.core.data.extensions.toRemoteLoadType
import com.masterplus.animals.core.domain.enums.ContentType
import com.masterplus.animals.core.domain.enums.RemoteLoadType
import com.masterplus.animals.core.domain.enums.RemoteSourceType
import com.masterplus.animals.core.domain.models.ItemOrder
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.shared_features.database.entity.RemoteKeyEntity
import java.io.IOException


sealed class RemoteMediatorError: Exception(){
    data object ReadLimitExceededException: RemoteMediatorError() {
        private fun readResolve(): Any = ReadLimitExceededException
    }
    data object NoInternetConnectionException: RemoteMediatorError() {
        private fun readResolve(): Any = NoInternetConnectionException
    }
}

abstract class BaseRemoteMediator<T: ItemOrder>(
    config: RemoteMediatorConfig,
    targetItemId: Int?
): BaseRemoteMediator2<T, T>(config, targetItemId)


@OptIn(ExperimentalPagingApi::class)
abstract class BaseRemoteMediator2<T: ItemOrder, D: ItemOrder>(
    config: RemoteMediatorConfig,
    private val targetItemId: Int?
): RemoteMediator<Int, T>() {

    protected val db = config.db
    protected val categoryRemoteSource = config.categoryRemoteSource
    protected val insertSpeciesHelper = config.insertSpeciesHelper
    private val readCounter = config.readCounter
    private val appConfigPreferences = config.appConfigPreferences
    private val connectivityObserver = config.connectivityObserver

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
        val remoteKey = db.remoteKeyDao.remoteKeyByQuery(saveRemoteKey)
        if(remoteKey?.should_refresh == true){
            return InitializeAction.LAUNCH_INITIAL_REFRESH
        }
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, T>): MediatorResult {
        return try {
            val remoteKey = db.remoteKeyDao.remoteKeyByQuery(saveRemoteKey)
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
                    remoteKey?.prev_key?.toIntOrNull() ?: return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                }
                LoadType.APPEND -> {
                    val key = remoteKey?.next_key?.toIntOrNull()
                    if(remoteKey != null && (remoteKey.is_next_key_end || remoteKey.next_key == null))return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
                    key
                }
            }
            if(!connectivityObserver.hasConnection()){
                return MediatorResult.Error(RemoteMediatorError.NoInternetConnectionException)
            }
            if(checkReadExceedLimit()){
                return MediatorResult.Error(RemoteMediatorError.ReadLimitExceededException)
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
                val (nextKey, isNextKeyEnd) = getNextKey(dataResponse, loadType, remoteKey)
                val prevKey = getPrevKey(dataResponse, loadType, remoteKey)
                val updatedRemoteKey = RemoteKeyEntity(
                    label = saveRemoteKey,
                    next_key = nextKey,
                    prev_key = prevKey,
                    is_next_key_end = isNextKeyEnd
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
        val paginationData = appConfigPreferences.getData().pagination
        val readExceedLimit = if(contentType.isContent) paginationData.readContentExceedLimit else paginationData.readCategoryExceedLimit
        return counter > readExceedLimit
    }

    open fun getNextKey(items: List<D>, loadType: LoadType,  remoteKey: RemoteKeyEntity?): Pair<String?, Boolean> {
        val newNextKey = items.lastOrNull()?.orderKey?.toString()
        val isNextKeyEnd = newNextKey == null
        return when (loadType) {
            LoadType.REFRESH, LoadType.APPEND -> Pair(newNextKey ?: remoteKey?.next_key, isNextKeyEnd)
            else -> Pair(remoteKey?.next_key, false)
        }
    }

    open fun getPrevKey(items: List<D>, loadType: LoadType,  remoteKey: RemoteKeyEntity?): String? {
        return when (loadType) {
            LoadType.REFRESH, LoadType.PREPEND -> items.firstOrNull()?.orderKey?.toString()
            else -> remoteKey?.prev_key
        }
    }
}
