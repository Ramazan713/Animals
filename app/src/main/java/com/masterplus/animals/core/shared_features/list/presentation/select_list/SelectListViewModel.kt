package com.masterplus.animals.core.shared_features.list.presentation.select_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.animals.core.shared_features.list.domain.repo.ListSpeciesRepo
import com.masterplus.animals.core.shared_features.list.domain.repo.ListRepo
import com.masterplus.animals.core.shared_features.list.domain.repo.ListViewRepo
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectListViewModel @Inject constructor(
    private val listSpeciesRepo: ListSpeciesRepo,
    private val listRepo: ListRepo,
    private val listViewRepo: ListViewRepo

): ViewModel(){

    private val _state = MutableStateFlow(SelectListState())
    val state: StateFlow<SelectListState> = _state.asStateFlow()

    private var loadDataJob: Job? = null


    fun onEvent(event: SelectListAction){
        when(event){
            is SelectListAction.AddToList -> {
                viewModelScope.launch {
                    listSpeciesRepo.addOrRemoveListSpecies(event.listView, event.animalId)
                }
            }
            is SelectListAction.LoadData -> {
                loadData(event)
            }
            is SelectListAction.NewList -> {
                viewModelScope.launch {
                    listRepo.insertList(event.listName)
                }
            }
            is SelectListAction.AddOrAskToList -> {
                viewModelScope.launch {
                    val listViewId = event.selectableListView.listView.id ?: return@launch
                    if(!event.selectableListView.isSelected) return@launch addToList(event)
                    if(_state.value.listIdControl != listViewId) return@launch addToList(event)
                    _state.update { state-> state.copy(
                        dialogEvent = SelectListDialogEvent.AskListDelete(
                            event.animalId,
                            event.selectableListView.listView
                        )
                    )}
                }
            }
            is SelectListAction.ShowDialog -> {
                _state.update { state-> state.copy(
                    dialogEvent = event.dialogEvent
                )}
            }
        }
    }
    private suspend fun addToList(event: SelectListAction.AddOrAskToList){
        listSpeciesRepo.addOrRemoveListSpecies(event.selectableListView.listView, event.animalId)
    }

    private fun loadData(event: SelectListAction.LoadData){
        loadDataJob?.cancel()
        loadDataJob = listViewRepo
            .getSelectableListViews(event.animalId)
            .onEach { lists ->
                _state.update { it.copy(
                    items = lists, listIdControl = event.listIdControl
                )}
            }
            .launchIn(viewModelScope)
    }
}