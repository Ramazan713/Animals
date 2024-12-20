package com.masterplus.animals.features.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.animals.core.shared_features.preferences.domain.SettingsPreferences
import com.masterplus.animals.core.shared_features.theme.domain.repo.ThemeRepo
import com.masterplus.animals.core.shared_features.translation.domain.repo.TranslationRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val themeRepo: ThemeRepo,
    private val translationRepo: TranslationRepo,
    private val settingsPreferences: SettingsPreferences
): ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    init {
        initData()
    }

    fun onAction(action: SettingsAction){
        when(action){
            SettingsAction.ClearMessage -> {
                _state.update { it.copy(
                    message = null
                ) }
            }
            SettingsAction.LoadData -> {}
            is SettingsAction.ShowDialog -> {
                _state.update { it.copy(
                    dialogEvent = action.dialogEvent
                ) }
            }

            is SettingsAction.SetDynamicTheme -> {
                viewModelScope.launch {
                    val updatedState =_state.updateAndGet { it.copy(
                        themeModel = it.themeModel.copy(
                            useDynamicColor = action.useDynamic
                        )
                    ) }
                    themeRepo.updateThemeModel(updatedState.themeModel)
                }
            }
            is SettingsAction.SetThemeEnum -> {
                viewModelScope.launch {
                    val updatedState =_state.updateAndGet { it.copy(
                        themeModel = it.themeModel.copy(
                            themeEnum = action.themeEnum
                        )
                    ) }
                    themeRepo.updateThemeModel(updatedState.themeModel)
                }
            }

            is SettingsAction.SetLanguage -> {
                viewModelScope.launch {
                    translationRepo.setLanguage(action.language)
                }
            }

            SettingsAction.NotShowBackupInitDialog -> {
                viewModelScope.launch {
                    settingsPreferences.updateData {
                        it.copy(showBackupSectionForLogin = false)
                    }
                }
            }

            SettingsAction.ResetDefaultValues -> {
                viewModelScope.launch {
                    settingsPreferences.clear()
                    initData()
                }
            }
        }
    }

    private fun initData(){
        viewModelScope.launch {
            val themeModel = themeRepo.getThemeModel()
            _state.update { state->
                state.copy(
                    themeModel = themeModel,
                )
            }
        }

        translationRepo
            .getFlowLanguage()
            .onEach { language->
                _state.update { it.copy(
                    language = language
                ) }
            }
            .launchIn(viewModelScope)

    }
}