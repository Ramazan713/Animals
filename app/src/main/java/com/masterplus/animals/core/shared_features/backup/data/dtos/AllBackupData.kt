package com.masterplus.animals.core.shared_features.backup.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class AllBackupData(
    val histories:  List<HistoryBackup>,
    val lists: List<ListBackup>,
    val listSpecies: List<ListSpeciesBackup>,
    val savePoints: List<SavePointBackup>,
    val settingsPreferences: SettingsDataBackup?
)
