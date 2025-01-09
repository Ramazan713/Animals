package com.masterplus.animals.features.search.domain.repo

import androidx.paging.PagingData
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.domain.models.SpeciesListDetail
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import kotlinx.coroutines.flow.Flow

interface SearchRepo {

    fun searchSpecies(
        query: String,
        pageSize: Int,
        language: LanguageEnum
    ): Flow<PagingData<SpeciesListDetail>>

    fun searchSpeciesWithCategory(
        query: String,
        pageSize: Int,
        categoryType: CategoryType,
        itemId: Int,
        language: LanguageEnum
    ): Flow<PagingData<SpeciesListDetail>>

    fun searchCategory(
        query: String,
        pageSize: Int,
        categoryType: CategoryType,
        language: LanguageEnum
    ): Flow<PagingData<CategoryData>>

    fun searchCategory(
        query: String,
        pageSize: Int,
        categoryType: CategoryType,
        itemId: Int,
        language: LanguageEnum
    ): Flow<PagingData<CategoryData>>

}