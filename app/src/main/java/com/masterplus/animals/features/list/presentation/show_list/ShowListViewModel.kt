package com.masterplus.animals.features.list.presentation.show_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.animals.R
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.shared_features.list.data.mapper.toListModel
import com.masterplus.animals.core.shared_features.list.domain.repo.ListRepo
import com.masterplus.animals.core.shared_features.list.domain.repo.ListViewRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShowListViewModel @Inject constructor(
    private val listRepo: ListRepo,
    private val listViewRepo: ListViewRepo
): ViewModel(){

    private val _state = MutableStateFlow(ShowListState())
    val state: StateFlow<ShowListState> = _state.asStateFlow()

    init {
        loadData()
    }

    fun onAction(action: ShowListAction){
        when(action){
            is ShowListAction.ShowDialog -> {
                _state.update { it.copy(
                    dialogEvent = action.dialogEvent
                )}
            }
            is ShowListAction.AddNewList -> {
                viewModelScope.launch {
                    listRepo.insertList(action.listName)
                    _state.update { it.copy(
                        message = UiText.Resource(R.string.successfully_added)
                    )}
                }
            }
            is ShowListAction.Archive -> {
                viewModelScope.launch {
                    listRepo.archiveList(action.listView.id ?: 0)
                    _state.update { it.copy(
                        message = UiText.Resource(R.string.successfully_archived)
                    )}
                }
            }
            is ShowListAction.Copy -> {
                viewModelScope.launch {
                    listRepo.copyList(action.listView.toListModel())
                    _state.update { it.copy(
                        message = UiText.Resource(R.string.successfully_copied)
                    )}
                }
            }
            is ShowListAction.Delete -> {
                viewModelScope.launch {
                    listRepo.deleteListById(action.listView.id ?: 0)
                    _state.update { it.copy(
                        message = UiText.Resource(R.string.successfully_deleted)
                    )}
                }
            }
            is ShowListAction.Rename -> {
                viewModelScope.launch {
                    listRepo.updateListName(action.listView.id ?: 0,action.newName)
                    _state.update { it.copy(
                        message = UiText.Resource(R.string.success)
                    )}
                }
            }
            is ShowListAction.ClearMessage -> {
                _state.update { it.copy(message = null)}
            }
        }
    }


    private fun loadData(){
        listViewRepo
            .getListViews(false)
            .onEach { items ->
                _state.update { it.copy(items = items)}
            }
            .launchIn(viewModelScope)
    }

}