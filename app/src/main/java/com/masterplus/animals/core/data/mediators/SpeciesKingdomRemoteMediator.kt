package com.masterplus.animals.core.data.mediators

import com.masterplus.animals.core.data.datasources.CategoryRemoteSource
import com.masterplus.animals.core.data.dtos.SpeciesDto
import com.masterplus.animals.core.data.utils.RemoteKeyUtil
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.shared_features.database.AppDatabase
import com.masterplus.animals.core.shared_features.database.entity_helper.SpeciesDetailEmbedded

class SpeciesKingdomRemoteMediator(
    db: AppDatabase,
    private val categoryRemoteSource: CategoryRemoteSource,
    private val kingdom: KingdomType,
    private val limit: Int
): BaseSpeciesRemoteMediator<SpeciesDetailEmbedded>(db) {
    override val saveRemoteKey: String
        get() = RemoteKeyUtil.getSpeciesKingdomRemoteKey(
            kingdom = kingdom
        )

    override suspend fun fetchData(startAfter: Int?): DefaultResult<List<SpeciesDto>> {
        return categoryRemoteSource.getSpeciesByKingdom(
            kingdomType = kingdom,
            limit = limit,
            loadKey = startAfter
        )
    }
}