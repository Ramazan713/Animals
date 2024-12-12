package com.masterplus.animals.features.settings.presentation.savepoint_settings

data class SavePointSettingsState(
    val isLoading: Boolean = false,
    val loadAutoSavePointForCategory: Boolean = true,
    val saveAutoSavePointForCategory: Boolean = true,
    val loadAutoSavePointForSpecies: Boolean = true,
    val saveAutoSavePointForSpecies: Boolean = true
)
