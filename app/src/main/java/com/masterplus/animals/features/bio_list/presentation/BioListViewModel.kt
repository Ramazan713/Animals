package com.masterplus.animals.features.bio_list.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.cachedIn
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.repo.CategoryRepo
import com.masterplus.animals.core.domain.repo.SpeciesRepo
import com.masterplus.animals.core.shared_features.list.domain.repo.ListSpeciesRepo
import com.masterplus.animals.core.shared_features.list.domain.use_cases.ListInFavoriteControlForDeletionUseCase
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import com.masterplus.animals.core.shared_features.savepoint.domain.repo.SavePointRepo
import com.masterplus.animals.features.bio_list.presentation.navigation.BioListRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BioListViewModel(
    private val speciesRepo: SpeciesRepo,
    private val categoryRepo: CategoryRepo,
    private val listSpeciesRepo: ListSpeciesRepo,
    private val listInFavoriteUseCase: ListInFavoriteControlForDeletionUseCase,
    private val savePointRepo: SavePointRepo,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val args = savedStateHandle.toRoute<BioListRoute>()
    
    private val _state = MutableStateFlow(BioListState())
    val state = _state.asStateFlow()

    val pagingItems = args.let { args->
        speciesRepo.getPagingSpeciesList(args.categoryType, args.realItemId, 20)
    }.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            val itemId = args.realItemId ?: return@launch
            val title = categoryRepo.getCategoryName(args.categoryType, itemId) ?: return@launch
            _state.update { it.copy(
                title = title
            ) }
        }
        _state.update { it.copy(
            listIdControl = if(args.categoryType == CategoryType.List) args.realItemId else null
        ) }
    }

    fun onAction(action: BioListAction){
        when(action){
            is BioListAction.ShowDialog -> {
                _state.update { it.copy(
                    dialogEvent = action.dialogEvent
                ) }
            }
            is BioListAction.AddOrAskFavorite -> {
                viewModelScope.launch {
                    listInFavoriteUseCase(_state.value.listIdControl,true).let { showDia->
                        if(showDia){
                            _state.update { state->
                                state.copy(
                                    dialogEvent = BioListDialogEvent.AskFavoriteDelete(action.animalId)
                                )
                            }
                        }else{
                            addFavoriteAnimal(action.animalId)
                        }
                    }
                }
            }
            is BioListAction.AddToFavorite -> {
                viewModelScope.launch {
                    addFavoriteAnimal(action.animalId)
                }
            }

            is BioListAction.SavePosition -> {
                viewModelScope.launch {
                    savePointRepo.insertContentSavePoint(
                        title = "Sample",
                        destination = SavePointDestination.fromCategoryType(
                            categoryType = args.categoryType,
                            destinationId = args.realItemId
                        ),
                        itemPosIndex = action.posIndex
                    )
                }
            }
        }
    }

    private suspend fun addFavoriteAnimal(wordId: Int){
        listSpeciesRepo.addOrRemoveFavoriteSpecies(wordId)
    }


}