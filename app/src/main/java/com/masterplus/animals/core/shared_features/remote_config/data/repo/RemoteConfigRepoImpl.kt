package com.masterplus.animals.core.shared_features.remote_config.data.repo

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.configUpdates
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.masterplus.animals.core.domain.constants.KPref
import com.masterplus.animals.core.domain.utils.safeCall
import com.masterplus.animals.core.shared_features.database.AppDatabase
import com.masterplus.animals.core.shared_features.preferences.data.get
import com.masterplus.animals.core.shared_features.preferences.domain.AppConfigPreferences
import com.masterplus.animals.core.shared_features.preferences.domain.AppPreferences
import com.masterplus.animals.core.shared_features.remote_config.domain.repo.RemoteConfigRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.tasks.await

class RemoteConfigRepoImpl(
    private val appConfigPreferences: AppConfigPreferences,
    private val appPreferences: AppPreferences,
    private val db: AppDatabase
): RemoteConfigRepo {

    private val remoteConfig = Firebase.remoteConfig

    override suspend fun init(){
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 60 * 60 * 12
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
    }

    override fun handleUpdates(scope: CoroutineScope){
        remoteConfig
            .configUpdates
            .onEach {
                safeCall {
                    val result = remoteConfig.activate().await()
                    if (!result)return@safeCall
                    updateConfigValues()
                    handleEvents()
                }
            }
            .launchIn(scope)
    }


    private suspend fun updateConfigValues(){
        val consumeIntervalSeconds = remoteConfig.getValue("consumeIntervalSeconds").asString().toIntOrNull()
        val thresholdConsumeSeconds = remoteConfig.getValue("thresholdConsumeSeconds").asString().toIntOrNull()
        val thresholdOpeningCount = remoteConfig.getValue("thresholdOpeningCount").asString().toIntOrNull()
        val speciesPageSize = remoteConfig.getValue("speciesPageSize").asString().toIntOrNull()
        val categoryPageSize = remoteConfig.getValue("categoryPageSize").asString().toIntOrNull()
        val homeCategoryPageSize = remoteConfig.getValue("homeCategoryPageSize").asString().toIntOrNull()
        val readExceedLimit = remoteConfig.getValue("readExceedLimit").asString().toIntOrNull()

        appConfigPreferences.updateData { it.copy(
            consumeIntervalSeconds = consumeIntervalSeconds ?: it.consumeIntervalSeconds,
            thresholdConsumeSeconds = thresholdConsumeSeconds ?: it.thresholdConsumeSeconds,
            thresholdOpeningCount = thresholdOpeningCount ?: it.thresholdOpeningCount,
            speciesPageSize = speciesPageSize ?: it.speciesPageSize,
            categoryPageSize = categoryPageSize ?: it.categoryPageSize,
            homeCategoryPageSize = homeCategoryPageSize ?: it.homeCategoryPageSize,
            readExceedLimit = readExceedLimit ?: it.readExceedLimit
        ) }
    }

    private suspend fun handleEvents(){
        val appData = appPreferences.getData()
        val localRemovePagingNextKeyEnd = appData[KPref.removePagingNextKeyEnd]
        val localSetPagingShouldRefresh = appData[KPref.setPagingShouldRefresh]

        val removePagingNextKeyEnd = remoteConfig.getValue("removePagingNextKeyEnd").asString()
        val setPagingShouldRefresh = remoteConfig.getValue("setPagingShouldRefresh").asString()

        if(removePagingNextKeyEnd.isNotBlank() && removePagingNextKeyEnd != localRemovePagingNextKeyEnd){
            db.remoteKeyDao.setAllNextKeyEnd(false)
            appPreferences.setItem(KPref.removePagingNextKeyEnd, removePagingNextKeyEnd)
        }

        if(setPagingShouldRefresh.isNotBlank() && setPagingShouldRefresh != localSetPagingShouldRefresh){
            db.remoteKeyDao.setAllShouldRefresh(true)
            appPreferences.setItem(KPref.setPagingShouldRefresh, setPagingShouldRefresh)
        }
    }
}