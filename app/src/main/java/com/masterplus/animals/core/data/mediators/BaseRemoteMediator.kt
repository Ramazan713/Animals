package com.masterplus.animals.core.data.mediators

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.shared_features.database.AppDatabase
import com.masterplus.animals.core.shared_features.database.entity.RemoteKeyEntity
import java.io.IOException


abstract class BaseRemoteMediator<T: Any>(
    override val db: AppDatabase,
): BaseRemoteMediator2<T, T>(db)

@OptIn(ExperimentalPagingApi::class)
abstract class BaseRemoteMediator2<T: Any, D: Any>(
    protected open val db: AppDatabase,
): RemoteMediator<Int, T>() {

    abstract val saveRemoteKey: String

    abstract suspend fun fetchData(startAfter: Int?): DefaultResult<List<D>>

    abstract fun getNextKey(items: List<D>): Int?

    abstract suspend fun insertData(items: List<D>)

    open suspend fun clearTable(){}

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, T>): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = db.withTransaction {
                        db.remoteKeyDao.remoteKeyByQuery(saveRemoteKey)
                    }
                    if (remoteKey == null){
                        null
                    }
                    else if(remoteKey.nextKey == null){
                        return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    }
                    else{
                        remoteKey.nextKey.toIntOrNull()
                    }
                }
            }
            val dataResponse = fetchData(loadKey).getSuccessData!!
            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    clearTable()
                }
                db.remoteKeyDao.insertOrReplace(
                    RemoteKeyEntity(
                        label = saveRemoteKey,
                        nextKey = getNextKey(dataResponse)?.toString()
                    )
                )
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
}