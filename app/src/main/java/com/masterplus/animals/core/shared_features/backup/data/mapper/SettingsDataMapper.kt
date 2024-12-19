package com.masterplus.animals.core.shared_features.backup.data.mapper

import com.masterplus.animals.core.shared_features.backup.data.dtos.SettingsDataBackup
import com.masterplus.animals.core.shared_features.preferences.domain.model.SettingsData


fun SettingsData.toBackupData(): SettingsDataBackup {
    return SettingsDataBackup(
        themeEnum = themeEnum,
        useThemeDynamic = useThemeDynamic,
        showBackupSectionForLogin = showBackupSectionForLogin,
    )
}


fun SettingsDataBackup.toData(): SettingsData{
    return SettingsData(
        themeEnum = themeEnum,
        useThemeDynamic = useThemeDynamic,
        showBackupSectionForLogin = showBackupSectionForLogin,
    )
}