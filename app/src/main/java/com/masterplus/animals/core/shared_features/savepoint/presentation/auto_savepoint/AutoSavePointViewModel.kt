package com.masterplus.animals.core.shared_features.savepoint.presentation.auto_savepoint

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.animals.core.shared_features.preferences.domain.SettingsPreferences
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointSaveMode
import com.masterplus.animals.core.shared_features.savepoint.domain.repo.SavePointRepo
import com.masterplus.animals.core.shared_features.savepoint.domain.use_cases.SavePointUpsertAutoModeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AutoSavePointViewModel(
    private val upsertAutoMode: SavePointUpsertAutoModeUseCase,
    private val settingsPreferences: SettingsPreferences,
    private val savePointRepo: SavePointRepo
): ViewModel(){

    private val _state = MutableStateFlow(AutoSavePointState())
    val state get() = _state.asStateFlow()

    fun onAction(action: AutoSavePointAction){
        when(action){
            is AutoSavePointAction.UpsertSavePoint -> {
                viewModelScope.launch {
                    if(!checkSaveSavePoint(action.contentType)) return@launch
                    upsertAutoMode(
                        destination = action.destination,
                        itemPosIndex = action.itemPosIndex,
                        contentType = action.contentType
                    )
                }
            }
            is AutoSavePointAction.LoadSavePoint -> {
                viewModelScope.launch {
                    if(!checkLoadSavePoint(action.contentType)) return@launch

                    if(action.initItemPos != 0){
                        _state.update { it.copy(initPos = action.initItemPos) }
                        return@launch
                    }

                    _state.update { it.copy(
                        loadingSavePointPos = true
                    ) }

                    val savePoint = savePointRepo.getSavePointByQuery(
                        destination = action.destination,
                        saveMode = SavePointSaveMode.Auto,
                        contentType = action.contentType
                    )
                    if(savePoint == null){
                        _state.update { it.copy(
                            loadingSavePointPos = false
                        ) }
                        return@launch
                    }

                    _state.update { state ->
                        state.copy(
                            uiEvent = AutoSavePointEvent.LoadItemPos(
                                pos = savePoint.itemPosIndex
                            ),
                            loadingSavePointPos = false
                        )
                    }
                }
            }

            AutoSavePointAction.ClearUiEvent -> {
                _state.update { it.copy(uiEvent = null) }
            }
        }
    }

    private suspend fun checkLoadSavePoint(contentType: SavePointContentType): Boolean{
        val settingsData = settingsPreferences.getData().savePointsSettingsData
        if(contentType.isContent) return settingsData.loadAutoSavePointForSpecies
        return settingsData.loadAutoSavePointForCategory
    }

    private suspend fun checkSaveSavePoint(contentType: SavePointContentType): Boolean{
        val settingsData = settingsPreferences.getData().savePointsSettingsData
        if(contentType.isContent) return settingsData.saveAutoSavePointForSpecies
        return settingsData.saveAutoSavePointForCategory
    }

}
