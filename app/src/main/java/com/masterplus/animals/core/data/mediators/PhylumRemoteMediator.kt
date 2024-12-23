package com.masterplus.animals.core.data.mediators

import com.masterplus.animals.core.data.datasources.CategoryRemoteSource
import com.masterplus.animals.core.data.utils.RemoteKeyUtil
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.shared_features.database.AppDatabase
import com.masterplus.animals.core.shared_features.database.entity_helper.PhylumWithImageEmbedded

class PhylumRemoteMediator(
    db: AppDatabase,
    private val kingdomType: KingdomType,
    private val categoryRemoteSource: CategoryRemoteSource,
    private val limit: Int
): BaseRemoteMediator<PhylumWithImageEmbedded>(db) {

    override val saveRemoteKey: String
        get() = RemoteKeyUtil.getPhylumRemoteKey(
            kingdomType = kingdomType,
        )

    override suspend fun fetchData(startAfter: Int?): DefaultResult<List<PhylumWithImageEmbedded>> {
        return categoryRemoteSource.getPhylums(
            kingdomType = kingdomType,
            limit = limit,
            startAfter = startAfter
        )
    }

    override suspend fun insertData(items: List<PhylumWithImageEmbedded>) {
        db.categoryDao.insertPhylumWithImages(items)
    }

    override fun getNextKey(items: List<PhylumWithImageEmbedded>): Int? {
        return items.lastOrNull()?.phylum?.id
    }

    override suspend fun clearTable() {
        db.categoryDao.deletePhylums(kingdomType.kingdomId)
    }
}