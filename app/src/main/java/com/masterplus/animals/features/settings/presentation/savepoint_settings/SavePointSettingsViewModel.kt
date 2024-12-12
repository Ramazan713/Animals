package com.masterplus.animals.features.settings.presentation.savepoint_settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.animals.core.shared_features.preferences.domain.SettingsPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SavePointSettingsViewModel(
    private val settingsPreferences: SettingsPreferences
): ViewModel(){

    private val _state = MutableStateFlow(SavePointSettingsState())
    val state get() = _state.asStateFlow()

    init {
        settingsPreferences
            .dataFlow
            .map { it.savePointsSettingsData }
            .distinctUntilChanged()
            .onEach { settingsData ->
                _state.update { it.copy(
                    loadAutoSavePointForSpecies = settingsData.loadAutoSavePointForSpecies,
                    saveAutoSavePointForSpecies = settingsData.saveAutoSavePointForSpecies,
                    loadAutoSavePointForCategory = settingsData.loadAutoSavePointForCategory,
                    saveAutoSavePointForCategory = settingsData.saveAutoSavePointForCategory
                ) }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: SavePointSettingsAction){
        when(action){
            is SavePointSettingsAction.ToggleCategoryLoadSavePoint -> {
                viewModelScope.launch {
                    settingsPreferences.updateData { it.copy(
                        savePointsSettingsData = it.savePointsSettingsData.copy(
                            loadAutoSavePointForCategory = action.value
                        )
                    ) }
                }
            }
            is SavePointSettingsAction.ToggleCategorySaveSavePoint -> {
                viewModelScope.launch {
                    settingsPreferences.updateData { it.copy(
                        savePointsSettingsData = it.savePointsSettingsData.copy(
                            saveAutoSavePointForCategory = action.value
                        )
                    ) }
                }
            }
            is SavePointSettingsAction.ToggleSpeciesLoadSavePoint -> {
                viewModelScope.launch {
                    settingsPreferences.updateData { it.copy(
                        savePointsSettingsData = it.savePointsSettingsData.copy(
                            loadAutoSavePointForSpecies = action.value
                        )
                    ) }
                }
            }
            is SavePointSettingsAction.ToggleSpeciesSaveSavePoint -> {
                viewModelScope.launch {
                    settingsPreferences.updateData { it.copy(
                        savePointsSettingsData = it.savePointsSettingsData.copy(
                            saveAutoSavePointForSpecies = action.value
                        )
                    ) }
                }
            }
        }
    }

}
