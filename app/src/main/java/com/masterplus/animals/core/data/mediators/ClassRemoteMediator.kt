package com.masterplus.animals.core.data.mediators

import androidx.paging.LoadType
import com.masterplus.animals.core.data.datasources.CategoryRemoteSource
import com.masterplus.animals.core.data.mapper.toClassWithImageEmbedded
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
import com.masterplus.animals.core.shared_features.database.entity_helper.ClassWithImageEmbedded

class ClassRemoteMediator(
    db: AppDatabase,
    private val kingdomType: KingdomType,
    private val phylumId: Int?,
    private val categoryRemoteSource: CategoryRemoteSource,
    readCounter: ServerReadCounter,
    targetItemId: Int? = null,
): BaseRemoteMediator<ClassWithImageEmbedded>(db, readCounter, targetItemId) {

    override val saveRemoteKey: String
        get() = RemoteKeyUtil.getClassRemoteKey(
            kingdomType = kingdomType,
            phylumId = phylumId
        )

    override val contentType: ContentType
        get() = ContentType.Category

    override suspend fun fetchData(
        loadKey: Int?,
        loadType: RemoteLoadType,
        sourceType: RemoteSourceType,
        limit: Int
    ): DefaultResult<List<ClassWithImageEmbedded>> {
        return categoryRemoteSource.getClasses(
            kingdomType = kingdomType,
            limit = limit,
            phylumId = phylumId,
            loadKey = loadKey,
            loadType = loadType,
            sourceType = sourceType
        ).map { items -> items.map { it.toClassWithImageEmbedded(saveRemoteKey) } }
    }

    override suspend fun isItemExists(itemId: Int, label: String): Boolean {
        return db.categoryDao.getClassWithId2(itemId, label) != null
    }

    override suspend fun insertData(items: List<ClassWithImageEmbedded>) {
        db.categoryDao.insertClassesWithImages(items)
    }

    override suspend fun clearTable() {
        db.categoryDao.deleteClasses(saveRemoteKey)
    }

}