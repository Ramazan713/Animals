package com.masterplus.animals.core.shared_features.preferences.data

import androidx.datastore.core.DataStore
import com.masterplus.animals.core.shared_features.preferences.domain.AppConfigPreferences
import com.masterplus.animals.core.shared_features.preferences.domain.model.AppConfigData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class AppConfigPreferencesImpl(
    private val pref: DataStore<AppConfigData>
): AppConfigPreferences {

    override val dataFlow: Flow<AppConfigData>
        get() = pref.data

    override suspend fun updateData(transform: suspend (t: AppConfigData) -> AppConfigData): AppConfigData {
        return pref.updateData {
            transform(it)
        }
    }

    override suspend fun clear(): AppConfigData {
        return pref.updateData {
            AppConfigData()
        }
    }

    override suspend fun getData(): AppConfigData {
        return pref.data.first()
    }
}