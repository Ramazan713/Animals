package com.masterplus.animals.core.data.mediators

import com.masterplus.animals.core.data.mapper.toPhylumWithImageEmbedded
import com.masterplus.animals.core.data.utils.RemoteKeyUtil
import com.masterplus.animals.core.domain.enums.ContentType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.enums.RemoteLoadType
import com.masterplus.animals.core.domain.enums.RemoteSourceType
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.domain.utils.map
import com.masterplus.animals.core.shared_features.database.entity_helper.PhylumWithImageEmbedded

class PhylumRemoteMediator(
    config: RemoteMediatorConfig,
    targetItemId: Int? = null,
    private val kingdomType: KingdomType,
): BaseRemoteMediator<PhylumWithImageEmbedded>(config, targetItemId) {

    override val saveRemoteKey: String
        get() = RemoteKeyUtil.getPhylumRemoteKey(
            kingdomType = kingdomType,
        )

    override val contentType: ContentType
        get() = ContentType.Category

    override suspend fun fetchData(
        loadKey: Int?,
        loadType: RemoteLoadType,
        sourceType: RemoteSourceType,
        limit: Int
    ): DefaultResult<List<PhylumWithImageEmbedded>> {
        return categoryRemoteSource.getPhylums(
            kingdomType = kingdomType,
            limit = limit,
            loadKey = loadKey,
            loadType = loadType,
            sourceType = sourceType
        ).map { items -> items.map { it.toPhylumWithImageEmbedded(saveRemoteKey) } }
    }

    override suspend fun isItemExists(itemId: Int, label: String): Boolean {
        return db.categoryDao.getPhylumWithId2(itemId, label) != null
    }

    override suspend fun insertData(items: List<PhylumWithImageEmbedded>) {
        db.categoryDao.insertPhylumWithImages(items)
    }

    override suspend fun clearTable() {
        db.categoryDao.deletePhylums(saveRemoteKey)
    }
}