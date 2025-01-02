package com.masterplus.animals.core.shared_features.preferences.data

import androidx.datastore.core.DataStore
import com.masterplus.animals.core.shared_features.preferences.domain.SettingsPreferences
import com.masterplus.animals.core.shared_features.preferences.domain.model.SettingsData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class SettingsPreferencesImpl(
    private val pref: DataStore<SettingsData>
): SettingsPreferences {

    override val dataFlow: Flow<SettingsData>
        get() = pref.data


    override suspend fun updateData(transform: suspend (t: SettingsData) -> SettingsData): SettingsData {
        return pref.updateData {
            transform(it)
        }
    }

    override suspend fun clear(): SettingsData {
        return pref.updateData {
            SettingsData()
        }
    }

    override suspend fun getData(): SettingsData {
        return pref.data.first()
    }
}