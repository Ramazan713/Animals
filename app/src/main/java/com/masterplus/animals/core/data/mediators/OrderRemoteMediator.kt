package com.masterplus.animals.core.data.mediators

import com.masterplus.animals.core.data.datasources.CategoryRemoteSource
import com.masterplus.animals.core.data.utils.RemoteKeyUtil
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.shared_features.database.AppDatabase
import com.masterplus.animals.core.shared_features.database.entity_helper.OrderWithImageEmbedded

class OrderRemoteMediator(
    db: AppDatabase,
    private val kingdomType: KingdomType,
    private val classId: Int?,
    private val categoryRemoteSource: CategoryRemoteSource,
    private val limit: Int
): BaseRemoteMediator<OrderWithImageEmbedded>(db) {

    override val saveRemoteKey: String
        get() = RemoteKeyUtil.getOrderRemoteKey(
            kingdomType = kingdomType,
            classId = classId
        )

    override suspend fun fetchData(startAfter: Int?): DefaultResult<List<OrderWithImageEmbedded>> {
        return categoryRemoteSource.getOrders(
            kingdomType = kingdomType,
            limit = limit,
            classId = classId,
            startAfter = startAfter,
            label = saveRemoteKey
        )
    }

    override suspend fun insertData(items: List<OrderWithImageEmbedded>) {
        db.categoryDao.insertOrdersWithImages(items)
    }

    override fun getNextKey(items: List<OrderWithImageEmbedded>): Int? {
        return items.lastOrNull()?.order?.id
    }

    override suspend fun clearTable() {
        db.categoryDao.deleteOrders(saveRemoteKey)
    }
}