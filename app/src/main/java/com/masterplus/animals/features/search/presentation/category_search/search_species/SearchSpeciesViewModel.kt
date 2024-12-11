package com.masterplus.animals.features.search.presentation.category_search.search_species

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.cachedIn
import com.masterplus.animals.core.domain.repo.CategoryRepo
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.shared_features.translation.domain.repo.TranslationRepo
import com.masterplus.animals.features.search.domain.enums.HistoryType
import com.masterplus.animals.features.search.domain.repo.HistoryRepo
import com.masterplus.animals.features.search.domain.repo.SearchRepo
import com.masterplus.animals.features.search.presentation.category_search.CategorySearchAction
import com.masterplus.animals.features.search.presentation.category_search.CategorySearchBaseViewModel
import com.masterplus.animals.features.search.presentation.category_search.CategorySearchState
import com.masterplus.animals.features.search.presentation.navigation.SearchSpeciesRoute
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchSpeciesViewModel(
    private val searchRepo: SearchRepo,
    private val categoryRepo: CategoryRepo,
    private val translationRepo: TranslationRepo,
    private val historyRepo: HistoryRepo,
    savedStateHandle: SavedStateHandle
): CategorySearchBaseViewModel(historyRepo, translationRepo) {

    val args = savedStateHandle.toRoute<SearchSpeciesRoute>()

    override val historyType: HistoryType
        get() = HistoryType.Content

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchResults = _state.map { it.query }
        .debounce(300L)
        .filter { it.isNotBlank() }
        .distinctUntilChanged()
        .flatMapLatest {
            val language = translationRepo.getLanguage()
            if(args.realItemId != null){
                searchRepo.searchSpeciesWithCategory(categoryType = args.categoryType, query = it, itemId = args.itemId, language = language)
            }else{
                searchRepo.searchSpeciesWithCategory(categoryType = args.categoryType, query = it, language = language)
            }
        }
        .cachedIn(viewModelScope)

    init {
        translationRepo
            .getFlowLanguage()
            .onEach { language ->
                val titleForPlaceholder =
                    categoryRepo.getCategoryName(args.categoryType, args.itemId, language)
                        ?.let { UiText.Text(it) }
                _state.update { it.copy(
                    titleForPlaceHolder = titleForPlaceholder
                ) }
            }
            .launchIn(viewModelScope)
    }
}