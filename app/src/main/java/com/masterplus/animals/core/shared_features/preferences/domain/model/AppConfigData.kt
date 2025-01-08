package com.masterplus.animals.core.shared_features.preferences.domain.model

import com.masterplus.animals.core.shared_features.preferences.domain.BaseAppSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import javax.inject.Singleton

@Serializable
data class AppConfigData(
    val pagination: ConfigPagination = ConfigPagination(),
    val ad: ConfigAd = ConfigAd()
)

@Serializable
data class ConfigPagination(
    val speciesPageSize: Int = 20,
    val categoryPageSize: Int = 20,
    val homeCategoryPageSize: Int = 8,
    val readContentExceedLimit: Int = 100,
    val readCategoryExceedLimit: Int = 50,
)

@Serializable
data class ConfigAd(
    val thresholdOpeningCount: Int = 15,
    val consumeIntervalSeconds: Int = 4,
    val thresholdConsumeSeconds: Int = 80,
    val rewardCategorySearchCount: Int = 2,
    val initRemainingCategorySearchCount: Int = 0,
    val rewardAppSearchCount: Int = 2,
    val initRemainingAppSearchCount: Int = 0
)

@Singleton
class AppConfigDataSerializer: BaseAppSerializer<AppConfigData>() {
    override val serializer: KSerializer<AppConfigData>
        get() = AppConfigData.serializer()
    override val defaultValue: AppConfigData
        get() = AppConfigData()
}
