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
                    _state.update { it.copy(isLoading = true) }
                    listRepo.insertList(action.listName)
                    _state.update { it.copy(
                        message = UiText.Resource(R.string.successfully_added),
                        isLoading = false
                    )}
                }
            }
            is ShowListAction.Archive -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    listRepo.archiveList(action.listView.id ?: 0)
                    _state.update { it.copy(
                        message = UiText.Resource(R.string.successfully_archived),
                        isLoading = false
                    )}
                }
            }
            is ShowListAction.Copy -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    listRepo.copyList(action.listView.toListModel())
                    _state.update { it.copy(
                        message = UiText.Resource(R.string.successfully_copied),
                        isLoading = false
                    )}
                }
            }
            is ShowListAction.Delete -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    listRepo.deleteListById(action.listView.id ?: 0)
                    _state.update { it.copy(
                        message = UiText.Resource(R.string.successfully_deleted),
                        isLoading = false
                    )}
                }
            }
            is ShowListAction.Rename -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    listRepo.updateListName(action.listView.id ?: 0,action.newName)
                    _state.update { it.copy(
                        message = UiText.Resource(R.string.success),
                        isLoading = false
                    )}
                }
            }
            is ShowListAction.ClearMessage -> {
                _state.update { it.copy(message = null)}
            }
        }
    }


    private fun loadData(){
        _state.update { it.copy(
            isLoading = true
        ) }
        listViewRepo
            .getListViews(false)
            .onEach { items ->
                _state.update { it.copy(
                    items = items,
                    isLoading = false
                )}
            }
            .launchIn(viewModelScope)
    }

}