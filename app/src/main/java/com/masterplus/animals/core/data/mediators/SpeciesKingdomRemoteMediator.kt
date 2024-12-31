package com.masterplus.animals.core.data.mediators

import com.masterplus.animals.core.data.dtos.SpeciesDto
import com.masterplus.animals.core.data.utils.RemoteKeyUtil
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.enums.RemoteLoadType
import com.masterplus.animals.core.domain.enums.RemoteSourceType
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.shared_features.database.entity_helper.SpeciesDetailEmbedded

class SpeciesKingdomRemoteMediator(
    config: RemoteMediatorConfig,
    targetItemId: Int? = null,
    private val kingdom: KingdomType,
): BaseSpeciesRemoteMediator<SpeciesDetailEmbedded>(config, targetItemId) {
    override val saveRemoteKey: String
        get() = RemoteKeyUtil.getSpeciesKingdomRemoteKey(
            kingdom = kingdom
        )

    override suspend fun fetchData(
        loadKey: Int?,
        loadType: RemoteLoadType,
        sourceType: RemoteSourceType,
        limit: Int
    ): DefaultResult<List<SpeciesDto>> {
        return categoryRemoteSource.getSpeciesByKingdom(
            kingdomType = kingdom,
            limit = limit,
            loadKey = loadKey,
            loadType = loadType,
            sourceType = sourceType
        )
    }
}