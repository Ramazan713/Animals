package com.masterplus.animals.core.data.mediators

import com.masterplus.animals.core.data.dtos.SpeciesDto
import com.masterplus.animals.core.data.utils.RemoteKeyUtil
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.enums.RemoteLoadType
import com.masterplus.animals.core.domain.enums.RemoteSourceType
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.shared_features.database.entity_helper.SpeciesDetailEmbedded

class SpeciesCategoryRemoteMediator(
    config: RemoteMediatorConfig,
    targetItemId: Int? = null,
    private val categoryType: CategoryType,
    private val kingdomType: KingdomType,
    private val itemId: Int,
): BaseSpeciesRemoteMediator<SpeciesDetailEmbedded>(config, targetItemId) {
    override val saveRemoteKey: String
        get() = RemoteKeyUtil.getSpeciesCategoryRemoteKey(
            categoryType = categoryType,
            itemId = itemId,
        )

    override suspend fun fetchData(
        loadKey: Int?,
        loadType: RemoteLoadType,
        sourceType: RemoteSourceType,
        limit: Int
    ): DefaultResult<List<SpeciesDto>> {
        return categoryRemoteSource.getSpeciesCategories(
            categoryType = categoryType,
            itemId = itemId,
            limit = limit,
            loadKey = loadKey,
            loadType = loadType,
            sourceType = sourceType,
            kingdomType = kingdomType
        )
    }
}