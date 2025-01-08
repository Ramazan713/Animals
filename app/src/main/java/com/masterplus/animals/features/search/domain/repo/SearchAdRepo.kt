package com.masterplus.animals.features.search.domain.repo

import kotlinx.coroutines.flow.Flow

interface SearchAdRepo {
    val remainingCategoryAdCount: Flow<Int>
    val remainingAppAdCount: Flow<Int>

    suspend fun resetCategoryAd()
    suspend fun decreaseCategoryAd()

    suspend fun resetAppAd()
    suspend fun decreaseAppAd()
}