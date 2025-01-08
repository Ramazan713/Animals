package com.masterplus.animals.features.search.domain.service

import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum

interface SearchSpeciesIndexService {

    suspend fun searchSpecies(
        query: String,
        categoryItemId: Int?,
        pageSize: Int,
        categoryType: CategoryType,
        kingdomType: KingdomType,
        languageEnum: LanguageEnum
    ): DefaultResult<List<String>>

    suspend fun searchCategories(
        query: String,
        parentItemId: Int?,
        pageSize: Int,
        categoryType: CategoryType,
        kingdomType: KingdomType,
        languageEnum: LanguageEnum
    ): DefaultResult<List<String>>
}