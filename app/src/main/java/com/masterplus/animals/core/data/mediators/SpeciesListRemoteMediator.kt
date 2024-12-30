package com.masterplus.animals.core.data.mediators

import androidx.paging.ExperimentalPagingApi
import com.masterplus.animals.core.data.datasources.CategoryRemoteSource
import com.masterplus.animals.core.data.dtos.SpeciesDto
import com.masterplus.animals.core.data.utils.RemoteKeyUtil
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.domain.utils.Result
import com.masterplus.animals.core.shared_features.database.AppDatabase
import com.masterplus.animals.core.shared_features.database.entity_helper.SpeciesDetailEmbedded

class SpeciesListRemoteMediator(
    db: AppDatabase,
    private val listId: Int,
    private val limit: Int,
    private val categoryRemoteSource: CategoryRemoteSource,
): BaseSpeciesRemoteMediator<SpeciesDetailEmbedded>(db) {
    override val saveRemoteKey: String
        get() = RemoteKeyUtil.DEFAULT

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun fetchData(startAfter: Int?): DefaultResult<List<SpeciesDto>> {
        val speciesIds = db.listSpeciesDao.getNotExistsSpeciesIds(listId, limit)
        if(speciesIds.isEmpty()) return Result.Success(listOf())
        return categoryRemoteSource.getSpecies(speciesIds, null,limit)
    }
}