package com.masterplus.animals.core.shared_features.remote_config.data.repo

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.configUpdates
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.masterplus.animals.core.shared_features.preferences.domain.AppConfigPreferences
import com.masterplus.animals.core.shared_features.remote_config.domain.repo.RemoteConfigRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.tasks.await

class RemoteConfigRepoImpl(
    private val appConfigPreferences: AppConfigPreferences
): RemoteConfigRepo {

    private val remoteConfig = Firebase.remoteConfig

    override fun handleUpdates(scope: CoroutineScope){
        remoteConfig
            .configUpdates
            .onEach {
                val result = remoteConfig.activate().await()
                if (!result)return@onEach
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
            .launchIn(scope)
    }
}