package com.masterplus.animals.features.settings.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel(

): ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

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
        }
    }
}