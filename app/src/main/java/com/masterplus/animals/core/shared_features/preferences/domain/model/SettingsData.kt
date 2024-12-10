package com.masterplus.animals.core.shared_features.preferences.domain.model

import com.masterplus.animals.core.shared_features.preferences.domain.BaseAppSerializer
import com.masterplus.animals.core.shared_features.theme.domain.enums.ThemeEnum
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import javax.inject.Singleton

@Serializable
data class SettingsData(
    val useThemeDynamic: Boolean = false,
    val themeEnum: ThemeEnum = ThemeEnum.defaultValue,
    val languageEnum: LanguageEnum = LanguageEnum.defaultValue,
    val loadAutoSavePoint: Boolean = true,
    val saveAutoSavePoint: Boolean = true
)


@Singleton
class SettingsDataSerializer: BaseAppSerializer<SettingsData>() {
    override val serializer: KSerializer<SettingsData>
        get() = SettingsData.serializer()
    override val defaultValue: SettingsData
        get() = SettingsData()
}