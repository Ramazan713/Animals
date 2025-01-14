package com.masterplus.animals.core.data.mediators

import com.masterplus.animals.core.data.mapper.toOrderWithImageEmbedded
import com.masterplus.animals.core.data.utils.RemoteKeyUtil
import com.masterplus.animals.core.domain.enums.ContentType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.enums.RemoteLoadType
import com.masterplus.animals.core.domain.enums.RemoteSourceType
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.domain.utils.map
import com.masterplus.animals.core.shared_features.database.entity_helper.OrderWithImageEmbedded

class OrderRemoteMediator(
    config: RemoteMediatorConfig,
    targetItemId: Int? = null,
    private val kingdomType: KingdomType,
    private val classId: Int?,
): BaseRemoteMediator<OrderWithImageEmbedded>(config, targetItemId) {

    override val saveRemoteKey: String
        get() = RemoteKeyUtil.getOrderRemoteKey(
            kingdomType = kingdomType,
            classId = classId
        )

    override val contentType: ContentType
        get() = ContentType.Category

    override suspend fun fetchData(
        loadKey: Int?,
        loadType: RemoteLoadType,
        sourceType: RemoteSourceType,
        limit: Int
    ): DefaultResult<List<OrderWithImageEmbedded>> {
        return categoryRemoteSource.getOrders(
            kingdomType = kingdomType,
            limit = limit,
            classId = classId,
            loadKey = loadKey,
            loadType = loadType,
            sourceType = sourceType
        ).map { items -> items.map { it.toOrderWithImageEmbedded(saveRemoteKey) } }
    }

    override suspend fun isItemExists(itemId: Int, label: String): Boolean {
        return db.categoryDao.getOrderWithId2(itemId, label) != null
    }

    override suspend fun insertData(items: List<OrderWithImageEmbedded>) {
        db.categoryDao.insertOrdersWithImages(items)
    }

    override suspend fun clearTable() {
        db.categoryDao.deleteOrders(saveRemoteKey)
    }
}