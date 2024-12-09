package com.masterplus.animals.features.savepoints.presentation.show_savepoints

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.masterplus.animals.R
import com.masterplus.animals.core.domain.models.IMenuItemEnum
import com.masterplus.animals.core.domain.models.IconInfo
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestinationType
import com.masterplus.animals.core.shared_features.savepoint.domain.repo.SavePointRepo
import com.masterplus.animals.features.savepoints.presentation.show_savepoints.navigation.ShowSavePointsRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ShowSavePointsViewModel(
    private val savePointRepo: SavePointRepo,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val args = savedStateHandle.toRoute<ShowSavePointsRoute>()

    private val _state = MutableStateFlow(ShowSavePointsState())
    val state = _state.asStateFlow()


    init {
        initData()
    }

    fun onAction(action: ShowSavePointsAction){
        when(action){
            is ShowSavePointsAction.Delete -> {
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
            is ShowSavePointsAction.EditTitle -> {
                viewModelScope.launch{
                    savePointRepo.updateSavePointTitle(action.savePoint.id ?:0 , action.title)
                    _state.update { it.copy(message = UiText.Resource(R.string.successfully_updated))}
                }
            }
            is ShowSavePointsAction.Select -> {
                _state.update { it.copy(selectedSavePoint = action.savePoint)}
            }
            is ShowSavePointsAction.ShowDialog -> {
                _state.update { state-> state.copy(
                    dialogEvent = action.dialogEvent,
                )}
            }
            is ShowSavePointsAction.ClearMessage -> {
                _state.update { it.copy(message = null)}
            }

            is ShowSavePointsAction.SelectDropdownMenuItem -> {
                _state.update { it.copy( selectedDropdownItem = action.item)}
            }
        }
    }

    private fun initData(){
        val allFilter = object : IMenuItemEnum {
            override val title: UiText
                get() = UiText.Text("Hepsi")
            override val iconInfo: IconInfo?
                get() = null
        }
        _state.update { it.copy(
            showDropdownMenu = true,
            dropdownItems = listOf(allFilter, *SavePointDestinationType.entries.toTypedArray()),
            selectedDropdownItem = allFilter
        ) }

        val requestedDataFlow = savePointRepo
            .getAllSavePointsByContentType(
                contentType = args.contentType,
                filteredDestinationTypeIds = null,
                kingdomType = args.kingdomType
            )

        combine(
            state.map { it.selectedDropdownItem },
            requestedDataFlow
        ){filter, savePoints->
            if(!_state.value.showDropdownMenu) return@combine savePoints
            if (filter is SavePointDestinationType){
                return@combine savePoints.filter { filter.destinationTypeId == it.destination.destinationTypeId }
            }
            return@combine savePoints
        }
        .onEach { savePoints ->
            _state.update { it.copy(
                savePoints = savePoints
            ) }
        }
        .launchIn(viewModelScope)
    }

}
