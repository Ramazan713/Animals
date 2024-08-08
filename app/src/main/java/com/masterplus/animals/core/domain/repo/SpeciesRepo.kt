package com.masterplus.animals.core.domain.repo

import androidx.paging.PagingData
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.models.SpeciesDetail
import kotlinx.coroutines.flow.Flow

interface SpeciesRepo {

    fun getPagingSpeciesList(categoryType: CategoryType, itemId: Int?, pageSize: Int): Flow<PagingData<SpeciesDetail>>
}