package com.masterplus.animals.core.shared_features.ad.data.repo

import com.masterplus.animals.core.domain.enums.ContentType
import com.masterplus.animals.core.shared_features.ad.domain.repo.ReadCounterRewardAdRepo
import com.masterplus.animals.core.shared_features.analytics.domain.repo.ServerReadCounter
import com.masterplus.animals.core.shared_features.preferences.domain.AppConfigPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class ReadCounterRewardAdRepoImpl(
    private val readCounter: ServerReadCounter,
    private val appConfigPreferences: AppConfigPreferences
): ReadCounterRewardAdRepo {

    private val readContentExceedLimitFlow = appConfigPreferences.dataFlow
        .map { it.pagination.readContentExceedLimit }
        .distinctUntilChanged()

    private val readCategoryExceedLimitFlow = appConfigPreferences.dataFlow
        .map { it.pagination.readCategoryExceedLimit }
        .distinctUntilChanged()

    override val categoryShowAdFlow: Flow<Boolean>
        get() = combine(
            readCounter.categoryCountersFlow,
            readCategoryExceedLimitFlow
        ){categoryCounters, readExceedLimit ->
            categoryCounters >= readExceedLimit
        }

    override val contentShowAdFlow: Flow<Boolean>
        get() = combine(
            readCounter.contentCountersFlow,
            readContentExceedLimitFlow
        ){contentCounters,  readExceedLimit ->
            contentCounters >= readExceedLimit
        }

    override suspend fun resetCounter(contentType: ContentType) {
        val paginationData = appConfigPreferences.getData().pagination
        val readExceedLimit = if(contentType.isContent) paginationData.readContentExceedLimit else paginationData.readCategoryExceedLimit
        readCounter.addCounter(contentType, -readExceedLimit)
    }

}