package com.masterplus.animals.core.data.repo

import com.masterplus.animals.core.data.datasources.CategoryRemoteSource
import com.masterplus.animals.core.data.utils.RemoteKeyUtil
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.repo.CategoryRemoteRepo
import com.masterplus.animals.core.domain.utils.EmptyDefaultResult
import com.masterplus.animals.core.domain.utils.map
import com.masterplus.animals.core.shared_features.database.TransactionProvider
import com.masterplus.animals.core.shared_features.database.dao.CategoryDao
import com.masterplus.animals.core.shared_features.database.dao.RemoteKeyDao
import com.masterplus.animals.core.shared_features.database.entity.RemoteKeyEntity

class FirebaseCategoryRemoteRepo(
    private val remoteKeyDao: RemoteKeyDao,
    private val categoryDao: CategoryDao,
    private val transactionProvider: TransactionProvider,
    private val categoryRemoteSource: CategoryRemoteSource
): CategoryRemoteRepo {
    override suspend fun getClasses(
        kingdomType: KingdomType,
        limit: Int,
        phylumId: Int?,
        refresh: Boolean
    ): EmptyDefaultResult {
        val saveKey = RemoteKeyUtil.getClassRemoteKey(
            kingdomType = kingdomType,
            phylumId = phylumId
        )
        val nextKey = getRemoteKey(saveKey, refresh)
        val response = categoryRemoteSource.getClasses(
            kingdomType = kingdomType,
            limit = limit,
            phylumId = phylumId,
            startAfter = nextKey,
            label = saveKey
        )
        if(response.isError) return response.map {  }

        val items = response.getSuccessData!!
        transactionProvider.runAsTransaction {
            remoteKeyDao.insertOrReplace(RemoteKeyEntity(
                label = saveKey,
                nextKey = items.lastOrNull()?.classEntity?.id?.toString()
            ))
            categoryDao.insertClassesWithImages(items)
        }
        return response.map {  }
    }

    override suspend fun getPhylums(
        kingdomType: KingdomType,
        limit: Int,
        startAfter: Int?,
        refresh: Boolean
    ): EmptyDefaultResult {
        val saveKey = RemoteKeyUtil.getPhylumRemoteKey(
            kingdomType = kingdomType,
        )
        val nextKey = getRemoteKey(saveKey, refresh)
        val response = categoryRemoteSource.getPhylums(
            kingdomType = kingdomType,
            limit = limit,
            startAfter = nextKey,
            label = saveKey
        )
        if(response.isError) return response.map {  }

        val items = response.getSuccessData!!
        transactionProvider.runAsTransaction {
            remoteKeyDao.insertOrReplace(RemoteKeyEntity(
                label = saveKey,
                nextKey = items.lastOrNull()?.phylum?.id?.toString()
            ))
            categoryDao.insertPhylumWithImages(items)
        }
        return response.map {  }
    }

    override suspend fun getOrders(
        kingdomType: KingdomType,
        limit: Int,
        classId: Int?,
        startAfter: Int?,
        refresh: Boolean
    ): EmptyDefaultResult {
        val saveKey = RemoteKeyUtil.getOrderRemoteKey(
            kingdomType = kingdomType,
            classId = classId
        )
        val nextKey = getRemoteKey(saveKey, refresh)
        val response = categoryRemoteSource.getOrders(
            kingdomType = kingdomType,
            limit = limit,
            classId = classId,
            startAfter = nextKey,
            label = saveKey
        )
        if(response.isError) return response.map {  }

        val items = response.getSuccessData!!
        transactionProvider.runAsTransaction {
            remoteKeyDao.insertOrReplace(RemoteKeyEntity(
                label = saveKey,
                nextKey = items.lastOrNull()?.order?.id?.toString()
            ))
            categoryDao.insertOrdersWithImages(items)
        }
        return response.map {  }
    }

    override suspend fun getFamilies(
        kingdomType: KingdomType,
        limit: Int,
        orderId: Int?,
        startAfter: Int?,
        refresh: Boolean
    ): EmptyDefaultResult {
        val saveKey = RemoteKeyUtil.getFamilyRemoteKey(
            kingdomType = kingdomType,
            orderId = orderId
        )
        val nextKey = getRemoteKey(saveKey, refresh)
        val response = categoryRemoteSource.getFamilies(
            kingdomType = kingdomType,
            limit = limit,
            orderId = orderId,
            startAfter = nextKey,
            label = saveKey
        )
        if(response.isError) return response.map {  }

        val items = response.getSuccessData!!
        transactionProvider.runAsTransaction {
            remoteKeyDao.insertOrReplace(RemoteKeyEntity(
                label = saveKey,
                nextKey = items.lastOrNull()?.family?.id?.toString()
            ))
            categoryDao.insertFamiliesWithImages(items)
        }
        return response.map {  }
    }

    override suspend fun getHabitats(
        kingdomType: KingdomType,
        limit: Int,
        startAfter: Int?,
        refresh: Boolean
    ): EmptyDefaultResult {
        val saveKey = RemoteKeyUtil.getHabitatRemoteKey(
            kingdomType = kingdomType,
        )
        val nextKey = getRemoteKey(saveKey, refresh)
        val response = categoryRemoteSource.getHabitats(
            kingdomType = kingdomType,
            limit = limit,
            startAfter = nextKey,
            label = saveKey
        )
        if(response.isError) return response.map {  }

        val items = response.getSuccessData!!
        transactionProvider.runAsTransaction {
            remoteKeyDao.insertOrReplace(RemoteKeyEntity(
                label = saveKey,
                nextKey = items.lastOrNull()?.habitat?.id?.toString()
            ))
            categoryDao.insertHabitatsWithImages(items)
        }
        return response.map {  }
    }


    private suspend fun getRemoteKey(saveKey: String, refresh: Boolean): Int? {
        return if(refresh) null else {
            remoteKeyDao.remoteKeyByQuery(saveKey)?.nextKey?.toIntOrNull()
        }
    }
}