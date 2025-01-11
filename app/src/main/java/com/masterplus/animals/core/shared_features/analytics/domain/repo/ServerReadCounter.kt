package com.masterplus.animals.core.shared_features.analytics.domain.repo

import com.masterplus.animals.core.domain.enums.ContentType
import kotlinx.coroutines.flow.Flow

interface ServerReadCounter {

    val contentCountersFlow: Flow<Int>
    val categoryCountersFlow: Flow<Int>

    suspend fun getCounter(contentType: ContentType): Int

    suspend fun addCounter(contentType: ContentType, number: Int)

    suspend fun resetCounter(contentType: ContentType)

    suspend fun checkNewDayAndReset(isNewDay: Boolean)
}