package com.masterplus.animals.core.shared_features.preferences.domain.model

import com.masterplus.animals.core.shared_features.preferences.domain.BaseAppSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import javax.inject.Singleton

@Serializable
data class AppConfigData(
    val speciesPageSize: Int = 20,
    val categoryPageSize: Int = 20,
    val homeCategoryPageSize: Int = 8,
    val readExceedLimit: Int = 150,
    val thresholdOpeningCount: Int = 15,
    val consumeIntervalSeconds: Int = 4,
    val thresholdConsumeSeconds: Int = 80
)

@Singleton
class AppConfigDataSerializer: BaseAppSerializer<AppConfigData>() {
    override val serializer: KSerializer<AppConfigData>
        get() = AppConfigData.serializer()
    override val defaultValue: AppConfigData
        get() = AppConfigData()
}
