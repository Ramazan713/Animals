package com.masterplus.animals.features.settings.presentation.savepoint_settings

sealed interface SavePointSettingsAction {

    data class ToggleCategoryLoadSavePoint(
        val value: Boolean
    ): SavePointSettingsAction

    data class ToggleCategorySaveSavePoint(
        val value: Boolean
    ): SavePointSettingsAction

    data class ToggleSpeciesLoadSavePoint(
        val value: Boolean
    ): SavePointSettingsAction

    data class ToggleSpeciesSaveSavePoint(
        val value: Boolean
    ): SavePointSettingsAction
}