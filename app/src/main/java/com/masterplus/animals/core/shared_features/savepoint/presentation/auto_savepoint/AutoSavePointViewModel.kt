package com.masterplus.animals.core.shared_features.savepoint.presentation.auto_savepoint

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.animals.core.domain.repo.ConnectivityObserver
import com.masterplus.animals.core.shared_features.ad.domain.repo.ReadCounterRewardAdRepo
import com.masterplus.animals.core.shared_features.preferences.domain.SettingsPreferences
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointSaveMode
import com.masterplus.animals.core.shared_features.savepoint.domain.repo.SavePointPosRepo
import com.masterplus.animals.core.shared_features.savepoint.domain.repo.SavePointRepo
import com.masterplus.animals.core.shared_features.savepoint.domain.use_cases.SavePointUpsertAutoModeUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AutoSavePointViewModel(
    private val upsertAutoMode: SavePointUpsertAutoModeUseCase,
    private val settingsPreferences: SettingsPreferences,
    private val savePointRepo: SavePointRepo,
    private val savePointPosRepo: SavePointPosRepo,
    private val readCounterRewardAdRepo: ReadCounterRewardAdRepo,
    private val coroutineScope: CoroutineScope,
    connectivityObserver: ConnectivityObserver
): ViewModel(){

    private val _state = MutableStateFlow(AutoSavePointState())
    val state get() = _state.asStateFlow()

    init {
        connectivityObserver
            .isConnected
            .distinctUntilChanged()
            .filter { it }
            .onEach {
                _state.update { it.copy(
                    uiEvent = AutoSavePointEvent.RetryPaging
                ) }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: AutoSavePointAction){
        when(action){
            is AutoSavePointAction.UpsertSavePoint -> {
                coroutineScope.launch {
                    if(!checkSaveSavePoint(action.contentType)) return@launch
                    upsertAutoMode(
                        destination = action.destination,
                        orderKey = action.orderKey,
                        contentType = action.contentType
                    )
                }
            }
            is AutoSavePointAction.LoadSavePoint -> {
                viewModelScope.launch {
                    if(state.value.isInitLoaded) return@launch
                    if(!checkLoadSavePoint(action.contentType)) return@launch

                    if(action.initOrderKey != _state.value.initOrderKey){
                        val uiEvent = getUiEventFromRequestNavToPos(
                            orderKey = action.initOrderKey,
                            contentType = action.contentType,
                            destination = action.destination
                        )
                        _state.update { it.copy(
                            initOrderKey = action.initOrderKey,
                            uiEvent = uiEvent,
                        ) }
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

                    val uiEvent = savePoint?.orderKey?.let { orderKey ->
                        getUiEventFromRequestNavToPos(
                            orderKey = orderKey,
                            contentType = action.contentType,
                            destination = action.destination
                        )
                    }
                    _state.update { state ->
                        state.copy(
                            uiEvent = uiEvent,
                            loadingSavePointPos = false,
                            isInitLoaded = true
                        )
                    }
                }
            }

            AutoSavePointAction.ClearUiEvent -> {
                _state.update { it.copy(uiEvent = null) }
            }

            is AutoSavePointAction.RequestNavigateToPosByOrderKey -> {
                viewModelScope.launch {
                    val config = _state.value.config ?: return@launch
                    val uiEvent = getUiEventFromRequestNavToPos(
                        orderKey = action.orderKey,
                        contentType = config.contentType,
                        destination = config.destination
                    )
                    _state.update { it.copy(
                        uiEvent = uiEvent
                    ) }
                }
            }
            is AutoSavePointAction.ShowAd -> {
                _state.update { it.copy(
                    uiEvent = AutoSavePointEvent.ShowAd,
                ) }
            }

            is AutoSavePointAction.SuccessShowAd -> {
                viewModelScope.launch {
                    readCounterRewardAdRepo.resetCounter(action.contentType)
                    _state.update { it.copy(
                        uiEvent = AutoSavePointEvent.RetryPaging
                    ) }
                }
            }

            is AutoSavePointAction.ShowDialog -> {
                _state.update { it.copy(dialogEvent = action.dialogEvent) }
            }

            is AutoSavePointAction.Init -> {
                _state.update { it.copy(
                    config = AutoSavePointConfig(destination = action.destination, contentType = action.contentType)
                ) }
            }
        }
    }

    private suspend fun getUiEventFromRequestNavToPos(
        orderKey: Int,
        contentType: SavePointContentType,
        destination: SavePointDestination
    ): AutoSavePointEvent{
        val pos = savePointPosRepo.getItemPos(
            orderKey = orderKey,
            contentType = contentType,
            destination = destination
        )
        if(pos != null){
            return AutoSavePointEvent.LoadItemPos(pos = pos)
        }
        return AutoSavePointEvent.LoadRequiredPage(orderKey = orderKey)
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
