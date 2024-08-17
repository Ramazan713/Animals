package com.masterplus.animals.core.domain.repo

import androidx.paging.PagingData
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.models.SpeciesDetail
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import kotlinx.coroutines.flow.Flow

interface SpeciesRepo {

    fun getPagingSpeciesList(
        categoryType: CategoryType,
        itemId: Int?,
        pageSize: Int,
        language: LanguageEnum
    ): Flow<PagingData<SpeciesDetail>>
}