package com.masterplus.animals.features.search.domain.repo

import androidx.paging.PagingData
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.domain.models.SpeciesDetail
import kotlinx.coroutines.flow.Flow

interface SearchRepo {

    fun searchSpeciesWithCategory(query: String, categoryType: CategoryType): Flow<PagingData<SpeciesDetail>>

    fun searchSpeciesWithCategory(query: String, categoryType: CategoryType, itemId: Int): Flow<PagingData<SpeciesDetail>>

    fun searchCategory(query: String, categoryType: CategoryType): Flow<PagingData<CategoryData>>

    fun searchCategory(query: String, categoryType: CategoryType, itemId: Int): Flow<PagingData<CategoryData>>

}