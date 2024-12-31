package com.masterplus.animals.core.data.mediators

import com.masterplus.animals.core.data.datasources.CategoryRemoteSource
import com.masterplus.animals.core.shared_features.analytics.domain.repo.ServerReadCounter
import com.masterplus.animals.core.shared_features.database.AppDatabase
import com.masterplus.animals.core.shared_features.preferences.domain.AppPreferences

data class RemoteMediatorConfig(
    val db: AppDatabase,
    val readCounter: ServerReadCounter,
    val appPreferences: AppPreferences,
    val categoryRemoteSource: CategoryRemoteSource
)
