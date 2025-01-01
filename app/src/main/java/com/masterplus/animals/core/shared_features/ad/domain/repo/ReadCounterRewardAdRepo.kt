package com.masterplus.animals.core.shared_features.ad.domain.repo

import com.masterplus.animals.core.domain.enums.ContentType
import kotlinx.coroutines.flow.Flow

interface ReadCounterRewardAdRepo {

    val contentShowAdFlow: Flow<Boolean>
    val categoryShowAdFlow: Flow<Boolean>

    suspend fun resetCounter(contentType: ContentType)
}