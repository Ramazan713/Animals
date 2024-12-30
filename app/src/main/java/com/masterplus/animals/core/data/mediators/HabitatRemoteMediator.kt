package com.masterplus.animals.core.data.mediators

import com.masterplus.animals.core.data.datasources.CategoryRemoteSource
import com.masterplus.animals.core.data.mapper.toFamilyWithImageEmbedded
import com.masterplus.animals.core.data.mapper.toHabitatCategoryWithImageEmbedded
import com.masterplus.animals.core.data.utils.RemoteKeyUtil
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.domain.utils.map
import com.masterplus.animals.core.shared_features.database.AppDatabase
import com.masterplus.animals.core.shared_features.database.entity_helper.HabitatWithImageEmbedded

class HabitatRemoteMediator(
    db: AppDatabase,
    private val kingdomType: KingdomType,
    private val categoryRemoteSource: CategoryRemoteSource,
    private val limit: Int
): BaseRemoteMediator<HabitatWithImageEmbedded>(db) {

    override val saveRemoteKey: String
        get() = RemoteKeyUtil.getHabitatRemoteKey(
            kingdomType = kingdomType,
        )

    override suspend fun fetchData(startAfter: Int?): DefaultResult<List<HabitatWithImageEmbedded>> {
        return categoryRemoteSource.getHabitats(
            kingdomType = kingdomType,
            limit = limit,
            loadKey = startAfter,
        ).map { items -> items.map { it.toHabitatCategoryWithImageEmbedded(saveRemoteKey) } }
    }

    override suspend fun insertData(items: List<HabitatWithImageEmbedded>) {
        db.categoryDao.insertHabitatsWithImages(items)
    }

    override fun getNextKey(items: List<HabitatWithImageEmbedded>): Int? {
        return items.lastOrNull()?.habitat?.id
    }

    override suspend fun clearTable() {
        db.categoryDao.deleteHabitats(saveRemoteKey)
    }
}