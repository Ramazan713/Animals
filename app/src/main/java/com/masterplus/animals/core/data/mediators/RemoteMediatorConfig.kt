package com.masterplus.animals.core.data.mediators

import com.masterplus.animals.core.data.datasources.CategoryRemoteSource
import com.masterplus.animals.core.domain.repo.ConnectivityObserver
import com.masterplus.animals.core.shared_features.analytics.domain.repo.ServerReadCounter
import com.masterplus.animals.core.shared_features.database.AppDatabase
import com.masterplus.animals.core.shared_features.preferences.domain.AppConfigPreferences

data class RemoteMediatorConfig(
    val db: AppDatabase,
    val readCounter: ServerReadCounter,
    val appConfigPreferences: AppConfigPreferences,
    val categoryRemoteSource: CategoryRemoteSource,
    val connectivityObserver: ConnectivityObserver
)
