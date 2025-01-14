package com.masterplus.animals.core.shared_features.preferences.domain

import kotlinx.coroutines.flow.Flow

interface BaseAppDataPreferences<T> {

    val dataFlow: Flow<T>

    suspend fun updateData(transform: suspend (t: T) -> T): T

    suspend fun clear(): T

    suspend fun getData(): T
}