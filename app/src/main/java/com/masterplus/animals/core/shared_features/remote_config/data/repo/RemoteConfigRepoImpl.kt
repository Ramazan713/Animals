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
import com.masterplus.animals.core.shared_features.preferences.domain.model.ConfigAd
import com.masterplus.animals.core.shared_features.preferences.domain.model.ConfigPagination
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
        val readContentExceedLimit = remoteConfig.getValue("readContentExceedLimit").asString().toIntOrNull()
        val readCategoryExceedLimit = remoteConfig.getValue("readCategoryExceedLimit").asString().toIntOrNull()

        appConfigPreferences.updateData { it.copy(
            ad = ConfigAd(
                consumeIntervalSeconds = consumeIntervalSeconds ?: it.ad.consumeIntervalSeconds,
                thresholdConsumeSeconds = thresholdConsumeSeconds ?: it.ad.thresholdConsumeSeconds,
                thresholdOpeningCount = thresholdOpeningCount ?: it.ad.thresholdOpeningCount,
            ),
            pagination = ConfigPagination(
                speciesPageSize = speciesPageSize ?: it.pagination.speciesPageSize,
                categoryPageSize = categoryPageSize ?: it.pagination.categoryPageSize,
                homeCategoryPageSize = homeCategoryPageSize ?: it.pagination.homeCategoryPageSize,
                readContentExceedLimit = readContentExceedLimit ?: it.pagination.readContentExceedLimit,
                readCategoryExceedLimit = readCategoryExceedLimit ?: it.pagination.readCategoryExceedLimit
            ),
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