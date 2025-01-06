package com.masterplus.animals.core.shared_features.select_font_size.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.animals.core.shared_features.select_font_size.domain.enums.FontSizeEnum
import com.masterplus.animals.core.shared_features.select_font_size.domain.repo.SelectFontSizeRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SelectFontSizeViewModel(
    private val selectFontSizeRepo: SelectFontSizeRepo,
): ViewModel(){

    private val _state = MutableStateFlow(SelectFontSizeState())
    val state get() = _state.asStateFlow()

    init {
        selectFontSizeRepo
            .fontSizeFlow
            .onEach { fontSizeEnum ->
                _state.update { it.copy(
                    lastSavedFontSizeEnum = fontSizeEnum
                ) }
            }
            .launchIn(viewModelScope)

        _state
            .map { it.currentFontSizeEnum != it.lastSavedFontSizeEnum }
            .distinctUntilChanged()
            .onEach { anyChanges ->
                _state.update { it.copy(
                    anyChanges = anyChanges
                ) }
            }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            val fontSizeEnum = selectFontSizeRepo.getFontSize()
            _state.update { it.copy(
                lastSavedFontSizeEnum = fontSizeEnum,
                currentFontSizeEnum = fontSizeEnum
            ) }
        }
    }

    fun onAction(action: SelectFontSizeAction){
        when(action){
            SelectFontSizeAction.ApplyChanges -> {
                viewModelScope.launch {
                    selectFontSizeRepo.changeFontSize(_state.value.currentFontSizeEnum)
                }
            }
            SelectFontSizeAction.RestoreChanges -> {
                _state.update { it.copy(
                    currentFontSizeEnum = it.lastSavedFontSizeEnum
                ) }
            }

            is SelectFontSizeAction.ChangeSliderPos -> {
                _state.update { it.copy(
                    currentFontSizeEnum = FontSizeEnum.fromKeyValue(action.pos.toInt())
                ) }
            }
        }
    }

}
