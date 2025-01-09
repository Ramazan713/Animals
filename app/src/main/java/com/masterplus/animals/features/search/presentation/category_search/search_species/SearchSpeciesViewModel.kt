package com.masterplus.animals.features.search.presentation.category_search.search_species

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import com.masterplus.animals.core.domain.models.SpeciesListDetail
import com.masterplus.animals.core.domain.repo.CategoryRepo
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.shared_features.preferences.domain.AppConfigPreferences
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import com.masterplus.animals.core.shared_features.translation.domain.repo.TranslationRepo
import com.masterplus.animals.features.search.domain.enums.HistoryType
import com.masterplus.animals.features.search.domain.repo.HistoryRepo
import com.masterplus.animals.features.search.domain.repo.SearchAdRepo
import com.masterplus.animals.features.search.domain.repo.SearchRemoteRepo
import com.masterplus.animals.features.search.domain.repo.SearchRepo
import com.masterplus.animals.features.search.presentation.category_search.CategorySearchBaseViewModel
import com.masterplus.animals.features.search.presentation.navigation.SearchSpeciesRoute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class SearchSpeciesViewModel(
    private val searchRepo: SearchRepo,
    private val categoryRepo: CategoryRepo,
    private val searchRemoteRepo: SearchRemoteRepo,
    searchAdRepo: SearchAdRepo,
    translationRepo: TranslationRepo,
    historyRepo: HistoryRepo,
    appConfigPreferences: AppConfigPreferences,
    savedStateHandle: SavedStateHandle
): CategorySearchBaseViewModel<SpeciesListDetail>(historyRepo, translationRepo, searchAdRepo, appConfigPreferences) {

    val args = savedStateHandle.toRoute<SearchSpeciesRoute>()

    override val historyType: HistoryType
        get() = HistoryType.Content

    override fun getLocalSearchResultFlow(
        query: String,
        languageEnum: LanguageEnum,
        pageSize: Int
    ): Flow<PagingData<SpeciesListDetail>> {
        return if(args.categoryItemId != null){
            searchRepo.searchSpeciesWithCategory(
                categoryType = args.categoryType,
                kingdomType = args.kingdomType,
                query = query,
                itemId = args.categoryItemId,
                language = languageEnum,
                pageSize = pageSize
            )
        }else{
            searchRepo.searchSpecies(
                query = query,
                language = languageEnum,
                pageSize = pageSize
            )
        }
    }

    override suspend fun getServerSearchResultFlow(
        query: String,
        languageEnum: LanguageEnum,
        localPageSize: Int,
        responsePageSize: Int
    ): DefaultResult<Flow<PagingData<SpeciesListDetail>>> {
        return searchRemoteRepo.searchSpeciesWithCategory(
            query = query,
            localPageSize = localPageSize,
            responsePageSize = responsePageSize,
            language = languageEnum,
            kingdomType = args.kingdomType,
            categoryType = args.categoryType,
            categoryItemId = args.categoryItemId
        )
    }

    init {
        translationRepo
            .getFlowLanguage()
            .onEach { language ->
                val titleForPlaceholder =
                    args.categoryItemId?.let {
                        categoryRepo.getCategoryName(args.categoryType, it, language)
                            ?.let { UiText.Text(it) }
                    }
                _state.update { it.copy(
                    titleForPlaceHolder = titleForPlaceholder
                ) }
            }
            .launchIn(viewModelScope)
    }
}