package com.masterplus.animals.core.shared_features.ad.data.repo

import com.masterplus.animals.core.domain.constants.KPref
import com.masterplus.animals.core.domain.enums.ContentType
import com.masterplus.animals.core.shared_features.ad.domain.repo.ReadCounterRewardAdRepo
import com.masterplus.animals.core.shared_features.analytics.domain.repo.ServerReadCounter
import com.masterplus.animals.core.shared_features.preferences.data.get
import com.masterplus.animals.core.shared_features.preferences.domain.AppPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class ReadCounterRewardAdRepoImpl(
    private val readCounter: ServerReadCounter,
    private val appPreferences: AppPreferences
): ReadCounterRewardAdRepo {

    private val readExceedLimitFlow = appPreferences.dataFlow
        .map { it[KPref.readExceedLimit] }
        .distinctUntilChanged()

    override val categoryShowAdFlow: Flow<Boolean>
        get() = combine(
            readCounter.categoryCountersFlow,
            readExceedLimitFlow
        ){categoryCounters, readExceedLimit ->
            categoryCounters >= readExceedLimit
        }

    override val contentShowAdFlow: Flow<Boolean>
        get() = combine(
            readCounter.contentCountersFlow,
            readExceedLimitFlow
        ){contentCounters,  readExceedLimit ->
            contentCounters >= readExceedLimit
        }

    override suspend fun resetCounter(contentType: ContentType) {
        val readExceedLimit = appPreferences.getItem(KPref.readExceedLimit)
        readCounter.addCounter(contentType, -readExceedLimit)
    }

}