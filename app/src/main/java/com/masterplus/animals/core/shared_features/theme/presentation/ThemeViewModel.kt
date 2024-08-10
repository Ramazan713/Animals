package com.masterplus.animals.core.shared_features.theme.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.animals.core.shared_features.preferences.domain.SettingsPreferences
import com.masterplus.animals.core.shared_features.theme.domain.models.ThemeModel
import com.masterplus.animals.core.shared_features.theme.domain.repo.ThemeRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class ThemeViewModel(
    private val settingsPreferences: SettingsPreferences,
    private val themeRepo: ThemeRepo
): ViewModel() {

    private val _state = MutableStateFlow(ThemeModel())
    val state = _state.asStateFlow()

    init {
        settingsPreferences.dataFlow
            .map {
                ThemeModel(
                    themeEnum = it.themeEnum,
                    useDynamicColor = it.useThemeDynamic,
                    enabledDynamicColor = themeRepo.hasSupportedDynamicTheme())
            }
            .distinctUntilChanged()
            .onEach { data ->
                _state.update { data}
            }
            .launchIn(viewModelScope)
    }
}