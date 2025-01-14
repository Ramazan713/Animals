package com.masterplus.animals.features.search.presentation.category_search.search_category

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import com.masterplus.animals.core.domain.enums.ContentType
import com.masterplus.animals.core.domain.models.CategoryData
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
import com.masterplus.animals.features.search.presentation.navigation.SearchCategoryRoute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class SearchCategoryViewModel(
    private val searchRepo: SearchRepo,
    private val categoryRepo: CategoryRepo,
    private val searchRemoteRepo: SearchRemoteRepo,
    searchAdRepo: SearchAdRepo,
    translationRepo: TranslationRepo,
    historyRepo: HistoryRepo,
    appConfigPreferences: AppConfigPreferences,
    savedStateHandle: SavedStateHandle,
): CategorySearchBaseViewModel<CategoryData>(
    historyRepo = historyRepo,
    translationRepo = translationRepo,
    searchAdRepo = searchAdRepo,
    appConfigPreferences = appConfigPreferences,
    categoryType = savedStateHandle.toRoute<SearchCategoryRoute>().categoryType
) {
    val args = savedStateHandle.toRoute<SearchCategoryRoute>()

    override val historyType: HistoryType
        get() = HistoryType.Category

    override val contentType: ContentType
        get() = ContentType.Category

    override fun getLocalSearchResultFlow(
        query: String,
        languageEnum: LanguageEnum,
        pageSize: Int
    ): Flow<PagingData<CategoryData>> {
        return if(args.categoryItemId != null){
            searchRepo.searchCategory(
                categoryType = args.categoryType,
                query = query,
                itemId = args.categoryItemId,
                language = languageEnum,
                pageSize = pageSize
            )
        }else{
            searchRepo.searchCategory(
                categoryType = args.categoryType,
                kingdomType = args.kingdomType,
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
    ): DefaultResult<Flow<PagingData<CategoryData>>> {
        return searchRemoteRepo.searchCategories(
            query = query,
            categoryType = args.categoryType,
            parentItemId = args.categoryItemId,
            kingdomType = args.kingdomType,
            language = languageEnum,
            localPageSize = localPageSize,
            responsePageSize = responsePageSize
        )
    }

    init {
        translationRepo
            .getFlowLanguage()
            .onEach { language ->
                val titleForPlaceholder = if(args.categoryItemId == null){
                    UiText.Text(args.categoryType.title)
                }
                else {
                    categoryRepo.getCategoryName(args.categoryType, args.categoryItemId, language)
                        ?.let { UiText.Text(it) }
                }
                _state.update { it.copy(
                    titleForPlaceHolder = titleForPlaceholder
                ) }
            }
            .launchIn(viewModelScope)
    }
}