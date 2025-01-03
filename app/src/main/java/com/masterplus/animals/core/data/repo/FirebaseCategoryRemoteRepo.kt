package com.masterplus.animals.core.data.repo

import com.masterplus.animals.core.data.datasources.CategoryRemoteSource
import com.masterplus.animals.core.data.mapper.toClassWithImageEmbedded
import com.masterplus.animals.core.data.mapper.toFamilyWithImageEmbedded
import com.masterplus.animals.core.data.mapper.toHabitatCategoryWithImageEmbedded
import com.masterplus.animals.core.data.mapper.toOrderWithImageEmbedded
import com.masterplus.animals.core.data.mapper.toPhylumWithImageEmbedded
import com.masterplus.animals.core.data.utils.RemoteKeyUtil
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.enums.RemoteLoadType
import com.masterplus.animals.core.domain.enums.RemoteSourceType
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
        loadKey: Int?,
        sourceType: RemoteSourceType,
        loadType: RemoteLoadType
    ): EmptyDefaultResult {
        val saveKey = RemoteKeyUtil.getClassRemoteKey(
            kingdomType = kingdomType,
            phylumId = phylumId
        )
        val nextKey = getRemoteKey(saveKey, loadType)
        val response = categoryRemoteSource.getClasses(
            kingdomType = kingdomType,
            limit = limit,
            phylumId = phylumId,
            loadKey = nextKey,
            loadType = loadType,
            sourceType = sourceType
        )
        if(response.isError) return response.map {  }

        val items = response.getSuccessData!!.map { it.toClassWithImageEmbedded(saveKey) }
        transactionProvider.runAsTransaction {
            remoteKeyDao.insertOrReplace(RemoteKeyEntity(
                label = saveKey,
                next_key = items.lastOrNull()?.classEntity?.id?.toString(),
                prev_key = items.firstOrNull()?.classEntity?.id?.toString()
            ))
            categoryDao.insertClassesWithImages(items)
        }
        return response.map {  }
    }

    override suspend fun getPhylums(
        kingdomType: KingdomType,
        limit: Int,
        loadKey: Int?,
        sourceType: RemoteSourceType,
        loadType: RemoteLoadType
    ): EmptyDefaultResult {
        val saveKey = RemoteKeyUtil.getPhylumRemoteKey(
            kingdomType = kingdomType,
        )
        val nextKey = getRemoteKey(saveKey, loadType)
        val response = categoryRemoteSource.getPhylums(
            kingdomType = kingdomType,
            limit = limit,
            loadKey = nextKey,
            loadType = loadType,
            sourceType = sourceType
        )
        if(response.isError) return response.map {  }

        val items = response.getSuccessData!!.map { it.toPhylumWithImageEmbedded(saveKey) }
        transactionProvider.runAsTransaction {
            remoteKeyDao.insertOrReplace(RemoteKeyEntity(
                label = saveKey,
                next_key = items.lastOrNull()?.phylum?.id?.toString(),
                prev_key = items.firstOrNull()?.phylum?.id?.toString()
            ))
            categoryDao.insertPhylumWithImages(items)
        }
        return response.map {  }
    }

    override suspend fun getOrders(
        kingdomType: KingdomType,
        limit: Int,
        classId: Int?,
        loadKey: Int?,
        sourceType: RemoteSourceType,
        loadType: RemoteLoadType
    ): EmptyDefaultResult {
        val saveKey = RemoteKeyUtil.getOrderRemoteKey(
            kingdomType = kingdomType,
            classId = classId
        )
        val nextKey = getRemoteKey(saveKey, loadType)
        val response = categoryRemoteSource.getOrders(
            kingdomType = kingdomType,
            limit = limit,
            classId = classId,
            loadKey = nextKey,
            loadType = loadType,
            sourceType = sourceType
        )
        if(response.isError) return response.map {  }

        val items = response.getSuccessData!!.map { it.toOrderWithImageEmbedded(saveKey) }
        transactionProvider.runAsTransaction {
            remoteKeyDao.insertOrReplace(RemoteKeyEntity(
                label = saveKey,
                next_key = items.lastOrNull()?.order?.id?.toString(),
                prev_key = items.firstOrNull()?.order?.id?.toString()
            ))
            categoryDao.insertOrdersWithImages(items)
        }
        return response.map {  }
    }

    override suspend fun getFamilies(
        kingdomType: KingdomType,
        limit: Int,
        orderId: Int?,
        loadKey: Int?,
        sourceType: RemoteSourceType,
        loadType: RemoteLoadType
    ): EmptyDefaultResult {
        val saveKey = RemoteKeyUtil.getFamilyRemoteKey(
            kingdomType = kingdomType,
            orderId = orderId
        )
        val nextKey = getRemoteKey(saveKey, loadType)
        val response = categoryRemoteSource.getFamilies(
            kingdomType = kingdomType,
            limit = limit,
            orderId = orderId,
            loadKey = nextKey,
            loadType = loadType,
            sourceType = sourceType
        )
        if(response.isError) return response.map {  }

        val items = response.getSuccessData!!.map { it.toFamilyWithImageEmbedded(saveKey) }
        transactionProvider.runAsTransaction {
            remoteKeyDao.insertOrReplace(RemoteKeyEntity(
                label = saveKey,
                next_key = items.lastOrNull()?.family?.id?.toString(),
                prev_key = items.firstOrNull()?.family?.id?.toString()
            ))
            categoryDao.insertFamiliesWithImages(items)
        }
        return response.map {  }
    }

    override suspend fun getHabitats(
        kingdomType: KingdomType,
        limit: Int,
        loadKey: Int?,
        sourceType: RemoteSourceType,
        loadType: RemoteLoadType
    ): EmptyDefaultResult {
        val saveKey = RemoteKeyUtil.getHabitatRemoteKey(
            kingdomType = kingdomType,
        )
        val nextKey = getRemoteKey(saveKey, loadType)
        val response = categoryRemoteSource.getHabitats(
            kingdomType = kingdomType,
            limit = limit,
            loadKey = nextKey,
            loadType = loadType,
            sourceType = sourceType
        )
        if(response.isError) return response.map {  }

        val items = response.getSuccessData!!.map { it.toHabitatCategoryWithImageEmbedded(saveKey) }
        transactionProvider.runAsTransaction {
            remoteKeyDao.insertOrReplace(RemoteKeyEntity(
                label = saveKey,
                next_key = items.lastOrNull()?.habitat?.id?.toString(),
                prev_key = items.firstOrNull()?.habitat?.id?.toString()
            ))
            categoryDao.insertHabitatsWithImages(items)
        }
        return response.map {  }
    }


    private suspend fun getRemoteKey(saveKey: String, loadType: RemoteLoadType): Int? {
        return if(loadType.isRefresh) null else {
            remoteKeyDao.remoteKeyByQuery(saveKey)?.next_key?.toIntOrNull()
        }
    }
}