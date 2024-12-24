package com.masterplus.animals.core.data.mediators

import com.masterplus.animals.core.data.datasources.CategoryRemoteSource
import com.masterplus.animals.core.data.utils.RemoteKeyUtil
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.shared_features.database.AppDatabase
import com.masterplus.animals.core.shared_features.database.entity_helper.ClassWithImageEmbedded

class ClassRemoteMediator(
    db: AppDatabase,
    private val kingdomType: KingdomType,
    private val phylumId: Int?,
    private val categoryRemoteSource: CategoryRemoteSource,
    private val limit: Int
): BaseRemoteMediator<ClassWithImageEmbedded>(db) {

    override val saveRemoteKey: String
        get() = RemoteKeyUtil.getClassRemoteKey(
            kingdomType = kingdomType,
            phylumId = phylumId
        )

    override suspend fun fetchData(startAfter: Int?): DefaultResult<List<ClassWithImageEmbedded>> {
        return categoryRemoteSource.getClasses(
            kingdomType = kingdomType,
            limit = limit,
            phylumId = phylumId,
            startAfter = startAfter,
            label = saveRemoteKey
        )
    }

    override suspend fun insertData(items: List<ClassWithImageEmbedded>) {
        db.categoryDao.insertClassesWithImages(items)
    }

    override fun getNextKey(items: List<ClassWithImageEmbedded>): Int? {
        return items.lastOrNull()?.classEntity?.id
    }

    override suspend fun clearTable() {
        db.categoryDao.deleteClasses(saveRemoteKey)
    }

}