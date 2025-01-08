package com.masterplus.animals.features.search.domain.repo

import androidx.paging.PagingData
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.domain.models.SpeciesListDetail
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import kotlinx.coroutines.flow.Flow

interface SearchRemoteRepo {

    suspend fun searchSpeciesWithCategory(
        query: String,
        categoryItemId: Int?,
        localPageSize: Int,
        responsePageSize: Int,
        categoryType: CategoryType,
        language: LanguageEnum,
        kingdomType: KingdomType
    ): DefaultResult<Flow<PagingData<SpeciesListDetail>>>

    suspend fun searchCategories(
        query: String,
        parentItemId: Int?,
        localPageSize: Int,
        responsePageSize: Int,
        categoryType: CategoryType,
        language: LanguageEnum,
        kingdomType: KingdomType
    ): DefaultResult<Flow<PagingData<CategoryData>>>
}