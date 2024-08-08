package com.masterplus.animals.core.shared_features.list.presentation.select_list_with_menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.animals.core.shared_features.list.domain.enums.SelectListMenuEnum
import com.masterplus.animals.core.shared_features.list.domain.repo.ListSpeciesRepo
import com.masterplus.animals.core.shared_features.list.domain.use_cases.ListInFavoriteControlForDeletionUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectListMenuViewModel @Inject constructor(
    private val listSpeciesRepo: ListSpeciesRepo,
    private val listInFavoriteUseCase: ListInFavoriteControlForDeletionUseCase
): ViewModel(){

    private val _state = MutableStateFlow(SelectListMenuState())
    val state: StateFlow<SelectListMenuState> = _state.asStateFlow()

    private var loadDataJob: Job? = null

    fun onEvent(action: SelectListMenuAction){
        when(action){
            is SelectListMenuAction.LoadData -> {
                loadData(action)
            }
            is SelectListMenuAction.AddToFavorite -> {
                viewModelScope.launch {
                    addFavoriteAnimal(action.animalId)
                }
            }
            is SelectListMenuAction.ShowDialog -> {
                _state.update { it.copy(dialogEvent = action.dialogEvent)}
            }
            is SelectListMenuAction.AddOrAskFavorite -> {
                viewModelScope.launch {
                    listInFavoriteUseCase(_state.value.listIdControl,true).let { showDia->
                        if(showDia){
                            _state.update { state->
                                state.copy(
                                    dialogEvent = SelectListMenuDialogEvent.AskFavoriteDelete(action.animalId)
                                )
                            }
                        }else{
                            addFavoriteAnimal(action.animalId)
                        }
                    }
                }
            }
        }
    }

    private suspend fun addFavoriteAnimal(wordId: Int){
        listSpeciesRepo.addOrRemoveFavoriteSpecies(wordId)
    }


    private fun loadData(event: SelectListMenuAction.LoadData){
        _state.update { it.copy(listIdControl = event.listId)}
        loadDataJob?.cancel()
        loadDataJob = listSpeciesRepo
            .getHasSpeciesInListFlow(event.animalId, true)
            .combine(listSpeciesRepo.getHasSpeciesInListFlow(event.animalId, false)){ inFavorite, inLists ->
                return@combine Pair(inFavorite, inLists)
            }
            .distinctUntilChanged()
            .onEach { pair ->
                _state.update { it.copy(
                    isFavorite = pair.first,
                    isAddedToList = pair.second,
                    listMenuItems = SelectListMenuEnum.getItems(
                        isFavorited = pair.first,
                        isListAdded = pair.second
                    )
                ) }
            }
            .launchIn(viewModelScope)
    }
}
