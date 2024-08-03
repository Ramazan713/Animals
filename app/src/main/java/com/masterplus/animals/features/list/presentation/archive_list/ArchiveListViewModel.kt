package com.masterplus.animals.features.list.presentation.archive_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.animals.R
import com.masterplus.animals.core.domain.utils.UiText
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

class ArchiveListViewModel @Inject constructor(
    private val listRepo: ListRepo,
    private val listViewRepo: ListViewRepo
): ViewModel(){

    private val _state = MutableStateFlow(ArchiveListState())
    val state: StateFlow<ArchiveListState> = _state.asStateFlow()


    init {
        loadData()
    }

    fun onAction(action: ArchiveListAction){
        when(action){
            is ArchiveListAction.ShowDialog -> {
                _state.update { it.copy(
                    dialogEvent = action.dialogEvent
                )}
            }
            is ArchiveListAction.UnArchive -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    listRepo.unArchiveList(action.listView.id ?: 0)
                    _state.update { it.copy(
                        message = UiText.Resource(R.string.successfully_unarchive),
                        isLoading = false
                    )}
                }
            }

            is ArchiveListAction.Delete -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    listRepo.deleteListById(action.listView.id ?: 0)
                    _state.update { it.copy(
                        message = UiText.Resource(R.string.successfully_deleted),
                        isLoading = false
                    )}
                }
            }
            is ArchiveListAction.Rename -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    listRepo.updateListName(action.listView.id ?: 0,action.newName)
                    _state.update { it.copy(
                        message = UiText.Resource(R.string.success),
                        isLoading = false
                    )}
                }
            }
            is ArchiveListAction.ClearMessage -> {
                _state.update { it.copy(message = null)}
            }
        }
    }


    private fun loadData(){
        _state.update { it.copy(
            isLoading = true
        ) }
        listViewRepo
            .getListViews(true)
            .onEach { items ->
                _state.update { it.copy(
                    items = items,
                    isLoading = false
                )}
            }
            .launchIn(viewModelScope)
    }

}