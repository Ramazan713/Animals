package com.masterplus.animals.core.shared_features.backup.data.dtos

import com.masterplus.animals.core.shared_features.theme.domain.enums.ThemeEnum
import kotlinx.serialization.Serializable

@Serializable
data class SettingsDataBackup(
    val themeEnum: ThemeEnum,
    val useThemeDynamic: Boolean,
    val showBackupSectionForLogin: Boolean,
)
