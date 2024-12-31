package com.masterplus.animals.core.data.mediators

import com.masterplus.animals.core.data.datasources.CategoryRemoteSource
import com.masterplus.animals.core.data.mapper.toFamilyWithImageEmbedded
import com.masterplus.animals.core.data.utils.RemoteKeyUtil
import com.masterplus.animals.core.domain.enums.ContentType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.enums.RemoteLoadType
import com.masterplus.animals.core.domain.enums.RemoteSourceType
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.domain.utils.map
import com.masterplus.animals.core.shared_features.analytics.domain.repo.ServerReadCounter
import com.masterplus.animals.core.shared_features.database.AppDatabase
import com.masterplus.animals.core.shared_features.database.entity_helper.FamilyWithImageEmbedded
import com.masterplus.animals.core.shared_features.preferences.domain.AppPreferences

class FamilyRemoteMediator(
    config: RemoteMediatorConfig,
    targetItemId: Int? = null,
    private val kingdomType: KingdomType,
    private val orderId: Int?,
): BaseRemoteMediator<FamilyWithImageEmbedded>(config, targetItemId) {

    override val saveRemoteKey: String
        get() = RemoteKeyUtil.getFamilyRemoteKey(
            kingdomType = kingdomType,
            orderId = orderId
        )
    override val contentType: ContentType
        get() = ContentType.Category

    override suspend fun fetchData(
        loadKey: Int?,
        loadType: RemoteLoadType,
        sourceType: RemoteSourceType,
        limit: Int
    ): DefaultResult<List<FamilyWithImageEmbedded>> {
        return categoryRemoteSource.getFamilies(
            kingdomType = kingdomType,
            limit = limit,
            orderId = orderId,
            loadKey = loadKey,
            loadType = loadType,
            sourceType = sourceType
        ).map { items -> items.map { it.toFamilyWithImageEmbedded(saveRemoteKey) } }
    }

    override suspend fun isItemExists(itemId: Int, label: String): Boolean {
        return db.categoryDao.getFamilyWithId2(itemId, label) != null
    }

    override suspend fun insertData(items: List<FamilyWithImageEmbedded>) {
        db.categoryDao.insertFamiliesWithImages(items)
    }

    override suspend fun clearTable() {
        db.categoryDao.deleteFamilies(saveRemoteKey)
    }
}