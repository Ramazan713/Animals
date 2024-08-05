package com.masterplus.animals.core.shared_features.savepoint.presentation.edit_savepoint

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.animals.R
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.repo.SavePointRepo
import com.masterplus.animals.core.shared_features.savepoint.domain.use_cases.SavePointSuggestedTitleUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditSavePointViewModel @Inject constructor(
    private val savePointRepo: SavePointRepo,
    private val suggestedTitleUseCase: SavePointSuggestedTitleUseCase
): ViewModel(){


    private val _state = MutableStateFlow(EditSavePointState())
    val state: StateFlow<EditSavePointState> = _state.asStateFlow()


    private var loadDataJob: Job? = null

    fun onAction(action: EditSavePointAction){
        when(action){
            is EditSavePointAction.Delete -> {
                viewModelScope.launch {
                    savePointRepo.deleteSavePoint(action.savePoint.id ?: 0)
                    _state.update { state->
                        val updatedSelectedSavePoint = if(action.savePoint == state.currentSelectedSavePoint) null else
                            state.currentSelectedSavePoint
                        state.copy(
                            message = UiText.Resource(R.string.successfully_deleted),
                            selectedSavePoint = updatedSelectedSavePoint
                        )
                    }
                }
            }
            is EditSavePointAction.EditTitle -> {
                viewModelScope.launch{
                    savePointRepo.updateSavePointTitle(action.savePoint.id ?:0 , action.title)
                    _state.update { it.copy(message = UiText.Resource(R.string.successfully_updated))}
                }
            }
            is EditSavePointAction.LoadData -> {
                loadData(action)
            }
            is EditSavePointAction.Select -> {
                _state.update { it.copy(selectedSavePoint = action.savePoint)}
            }
            is EditSavePointAction.AddSavePoint -> {
                viewModelScope.launch {
                    savePointRepo.insertContentSavePoint(
                        title = action.title,
                        itemPosIndex = action.posIndex,
                        destination = state.value.loadParam?.destination ?: return@launch,
                        dateTime = action.currentDateTime
                    )
                    _state.update { it.copy(message = UiText.Resource(R.string.successfully_added))}
                }
            }
            is EditSavePointAction.OverrideSavePoint -> {
                viewModelScope.launch {
                    _state.value.currentSelectedSavePoint?.let { savePoint ->
                        savePointRepo.updateSavePointPos(savePoint.id ?: 0, action.pos)
                        _state.update{ it.copy(message = UiText.Resource(R.string.successfully_updated)) }
                    }
                }
            }
            is EditSavePointAction.ShowDialog -> {
                _state.update { state-> state.copy(
                    dialogEvent = action.dialogEvent,
                )}
            }
            is EditSavePointAction.ClearMessage -> {
                _state.update { it.copy(message = null)}
            }

            EditSavePointAction.RequestAddNewSavePoint -> {
                viewModelScope.launch {
                    val param = state.value.loadParam ?: return@launch
                    val suggestedData = suggestedTitleUseCase(
                        destinationId = param.destinationId,
                        destinationTypeId = param.destinationTypeId,
                        savePointContentType = param.contentType
                    )
                    _state.update { it.copy(
                        dialogEvent = EditSavePointDialogEvent.AddSavePointTitle(
                            suggestedTitle = suggestedData.title,
                            currentDateTime = suggestedData.currentDateTime
                        )
                    ) }
                }
            }
        }
    }


    private fun loadData(action: EditSavePointAction.LoadData){
        _state.update { it.copy(
            loadParam = action.loadPram,
            showImage = action.loadPram.filteredDestinationTypeIds?.size != 1
        ) }
        loadDataJob?.cancel()
        loadDataJob = savePointRepo
            .getAllSavePointsByContentType(
                contentType = action.loadPram.contentType,
                filteredDestinationTypeIds = action.loadPram.filteredDestinationTypeIds
            )
            .onEach { savePoints ->
                _state.update { it.copy(savePoints = savePoints)}
            }
            .launchIn(viewModelScope)
    }

}