package com.masterplus.animals.features.search.presentation.category_search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.cachedIn
import com.masterplus.animals.core.domain.repo.CategoryRepo
import com.masterplus.animals.features.search.domain.repo.SearchRepo
import com.masterplus.animals.features.search.presentation.navigation.CategorySpeciesSearchRoute
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategorySpeciesSearchViewModel(
    private val searchRepo: SearchRepo,
    private val categoryRepo: CategoryRepo,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val args = savedStateHandle.toRoute<CategorySpeciesSearchRoute>()

    private val _state = MutableStateFlow(CategorySearchState())
    val state = _state.asStateFlow()


    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchResults = _state.map { it.query }
        .debounce(300L)
        .distinctUntilChanged()
        .flatMapLatest {
            if(args.realItemId != null){
                searchRepo.searchSpeciesWithCategory(categoryType = args.categoryType, query = it, itemId = args.itemId)
            }else{
                searchRepo.searchSpeciesWithCategory(categoryType = args.categoryType, query = it)
            }
        }
        .cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            val titleForPlaceholder = categoryRepo.getCategoryName(args.categoryType, args.itemId)
            _state.update { it.copy(
                titleForPlaceHolder = titleForPlaceholder
            ) }
        }
    }

    fun onAction(action: CategorySearchAction){
        when(action){
            is CategorySearchAction.SearchQuery -> {
                _state.update { it.copy(
                    query = action.query
                ) }
            }
        }
    }
}