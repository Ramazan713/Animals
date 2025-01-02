package com.masterplus.animals.core.shared_features.preferences.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.MultiProcessDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.masterplus.animals.core.shared_features.preferences.data.AppConfigPreferencesImpl
import com.masterplus.animals.core.shared_features.preferences.data.AppPreferencesImpl
import com.masterplus.animals.core.shared_features.preferences.data.SettingsPreferencesImpl
import com.masterplus.animals.core.shared_features.preferences.domain.AppConfigPreferences
import com.masterplus.animals.core.shared_features.preferences.domain.AppPreferences
import com.masterplus.animals.core.shared_features.preferences.domain.SettingsPreferences
import com.masterplus.animals.core.shared_features.preferences.domain.model.AppConfigDataSerializer
import com.masterplus.animals.core.shared_features.preferences.domain.model.SettingsDataSerializer
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import java.io.File

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("appPreferences")


val preferencesDataModule = module {
    single<AppPreferences> {
        AppPreferencesImpl(androidApplication().dataStore)
    }

    single<SettingsPreferences> {
        val settingsDataStore = MultiProcessDataStoreFactory.create(
            SettingsDataSerializer(),
            produceFile = {
                File("${androidApplication().filesDir.path}/myapp.settingsPreferences_pb")
            }
        )
        SettingsPreferencesImpl(settingsDataStore)
    }

    single<AppConfigPreferences> {
        val settingsDataStore = MultiProcessDataStoreFactory.create(
            AppConfigDataSerializer(),
            produceFile = {
                File("${androidApplication().filesDir.path}/myapp.appConfigPreferences_pb")
            }
        )
        AppConfigPreferencesImpl(settingsDataStore)
    }
}