package com.masterplus.animals.core.shared_features.add_species_to_list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.animals.core.shared_features.list.domain.repo.ListSpeciesRepo
import com.masterplus.animals.core.shared_features.list.domain.use_cases.ListInFavoriteControlForDeletionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddSpeciesToListViewModel(
    private val listInFavoriteUseCase: ListInFavoriteControlForDeletionUseCase,
    private val listSpeciesRepo: ListSpeciesRepo,
): ViewModel() {

    private val _state = MutableStateFlow(AddSpeciesToListState())
    val state = _state.asStateFlow()

    fun onAction(action: AddSpeciesToListAction){
        when(action){
            is AddSpeciesToListAction.AddOrAskFavorite -> {
                viewModelScope.launch {
                    listInFavoriteUseCase(_state.value.listIdControl,true).let { showDia->
                        if(showDia){
                            _state.update { state->
                                state.copy(
                                    dialogEvent = AddSpeciesToListDialogEvent.AskFavoriteDelete(action.speciesId)
                                )
                            }
                        }else{
                            addFavoriteAnimal(action.speciesId)
                        }
                    }
                }
            }
            is AddSpeciesToListAction.AddToFavorite -> {
                viewModelScope.launch {
                    addFavoriteAnimal(action.speciesId)
                }
            }
            is AddSpeciesToListAction.ShowDialog -> {
                _state.update { it.copy(
                    dialogEvent = action.dialogEvent
                ) }
            }
            is AddSpeciesToListAction.SetListIdControl ->  {
                _state.update { it.copy(
                    listIdControl = action.listIdControl
                ) }
            }
        }
    }

    private suspend fun addFavoriteAnimal(wordId: Int){
        listSpeciesRepo.addOrRemoveFavoriteSpecies(wordId)
    }
}