package com.masterplus.animals.core.domain.repo

import androidx.paging.PagingData
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.SpeciesListDetail
import com.masterplus.animals.core.domain.models.SpeciesModel
import com.masterplus.animals.core.domain.utils.EmptyDefaultResult
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import kotlinx.coroutines.flow.Flow

interface SpeciesRepo {

    fun getPagingSpeciesWithKingdom(
        pageSize: Int,
        language: LanguageEnum,
        kingdom: KingdomType,
        targetItemId: Int? = null
    ): Flow<PagingData<SpeciesListDetail>>

    fun getPagingSpeciesWithList(
        itemId: Int,
        pageSize: Int,
        language: LanguageEnum,
        targetItemId: Int? = null
    ): Flow<PagingData<SpeciesListDetail>>

    fun getPagingSpeciesWithCategory(
        categoryType: CategoryType,
        itemId: Int,
        pageSize: Int,
        language: LanguageEnum,
        kingdom: KingdomType,
        targetItemId: Int? = null
    ): Flow<PagingData<SpeciesListDetail>>

    suspend fun getSpeciesById(speciesId: Int, lang: LanguageEnum): SpeciesModel?

    suspend fun checkSpeciesDetailData(species: SpeciesModel): EmptyDefaultResult
}