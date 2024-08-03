package com.masterplus.animals.features.bio_list.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.cachedIn
import com.masterplus.animals.core.domain.repo.AnimalRepo
import com.masterplus.animals.core.domain.repo.CategoryRepo
import com.masterplus.animals.core.shared_features.list.domain.repo.ListAnimalsRepo
import com.masterplus.animals.features.bio_list.presentation.navigation.BioListRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BioListViewModel(
    private val animalRepo: AnimalRepo,
    private val categoryRepo: CategoryRepo,
    private val listAnimalsRepo: ListAnimalsRepo,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val args = savedStateHandle.toRoute<BioListRoute>()
    
    private val _state = MutableStateFlow(BioListState())
    val state = _state.asStateFlow()

    val pagingItems = args.let { args->
        animalRepo.getPagingAnimalsList(args.categoryType, args.realItemId, 20)
    }.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            val itemId = args.realItemId ?: return@launch
            val title = categoryRepo.getCategoryName(args.categoryType, itemId) ?: return@launch
            _state.update { it.copy(
                title = title
            ) }
        }
    }

    fun onAction(action: BioListAction){
        when(action){
            is BioListAction.ShowDialog -> {
                _state.update { it.copy(
                    dialogEvent = action.dialogEvent
                ) }
            }

            is BioListAction.FavoriteItem -> {
                viewModelScope.launch {
                    listAnimalsRepo.addOrRemoveFavoriteAnimal(action.animalData.id?:0)
                }
            }
        }
    }


}