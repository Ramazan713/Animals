package com.masterplus.animals.core.data.mediators

import com.masterplus.animals.core.data.datasources.CategoryRemoteSource
import com.masterplus.animals.core.data.dtos.SpeciesDto
import com.masterplus.animals.core.data.mapper.toFamilyWithImageEmbedded
import com.masterplus.animals.core.data.mapper.toSpeciesImageWithMetadataEmbedded
import com.masterplus.animals.core.data.utils.RemoteKeyUtil
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.domain.utils.map
import com.masterplus.animals.core.shared_features.database.AppDatabase
import com.masterplus.animals.core.shared_features.database.entity_helper.SpeciesDetailEmbedded

class SpeciesCategoryRemoteMediator(
    db: AppDatabase,
    private val categoryRemoteSource: CategoryRemoteSource,
    private val categoryType: CategoryType,
    private val itemId: Int,
    private val limit: Int
): BaseSpeciesRemoteMediator<SpeciesDetailEmbedded>(db) {
    override val saveRemoteKey: String
        get() = RemoteKeyUtil.getSpeciesCategoryRemoteKey(
            categoryType = categoryType,
            itemId = itemId,
        )

    override suspend fun fetchData(startAfter: Int?): DefaultResult<List<SpeciesDto>> {
        return categoryRemoteSource.getSpeciesCategories(
            categoryType = categoryType,
            itemId = itemId,
            limit = limit,
            loadKey = startAfter
        )
    }
}