package com.masterplus.animals.features.search.presentation.category_search

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.ContentType
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.shared_features.preferences.domain.AppConfigPreferences
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import com.masterplus.animals.core.shared_features.translation.domain.repo.TranslationRepo
import com.masterplus.animals.features.search.domain.enums.HistoryType
import com.masterplus.animals.features.search.domain.enums.SearchType
import com.masterplus.animals.features.search.domain.repo.HistoryRepo
import com.masterplus.animals.features.search.domain.repo.SearchAdRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
abstract class CategorySearchBaseViewModel<T: Any>(
    private val historyRepo: HistoryRepo,
    private val translationRepo: TranslationRepo,
    private val searchAdRepo: SearchAdRepo,
    private val appConfigPreferences: AppConfigPreferences,
    private val categoryType: CategoryType
): ViewModel() {

    abstract val historyType: HistoryType
    abstract val contentType: ContentType

    private val serverSearchedQueryFlow = MutableStateFlow("")

    protected val _state = MutableStateFlow(CategorySearchState())
    val state = _state.asStateFlow()

    private val queryTextFlow = snapshotFlow { _state.value.queryState.text.toString() }

    private val localPageSizeFlow = appConfigPreferences.dataFlow
        .map { it.pagination.searchCategoryLocalPageSize }
        .distinctUntilChanged()

    private val responsePageSizeFlow = appConfigPreferences.dataFlow
        .map { it.pagination.searchCategoryResponsePageSize }
        .distinctUntilChanged()

    private val localSearchResultFlow = queryTextFlow
        .debounce(300L)
        .filter { it.isNotBlank() }
        .distinctUntilChanged()
        .combine(localPageSizeFlow){ query, pageSize ->
            Pair(query, pageSize)
        }
        .flatMapLatest { pair ->
            val language = translationRepo.getLanguage()
            getLocalSearchResultFlow(query = pair.first, languageEnum = language, pageSize = pair.second)
        }
        .cachedIn(viewModelScope)

    private val serverSearchResultFlow = combine(
        serverSearchedQueryFlow.filter { it.isNotBlank() },
        localPageSizeFlow,
        responsePageSizeFlow
    ){ query, localPageSize, responsePageSize->
        Triple(query, localPageSize, responsePageSize)
    }
    .flatMapLatest { triple ->
        _state.update { it.copy(isRemoteSearching = true) }
        val language = translationRepo.getLanguage()
        val response = getServerSearchResultFlow(query = triple.first, languageEnum = language, localPageSize = triple.second, responsePageSize = triple.third,)
        response.getFailureError?.let { error ->
            _state.update { it.copy(
                message = error.text
            ) }
        }
        response.onSuccessAsync {
            searchAdRepo.decreaseCategoryAd()
        }
        _state.update { it.copy(isRemoteSearching = false) }
        response.getSuccessData ?: emptyFlow()
    }
    .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val searchResults = _state
        .map { it.searchType }
        .distinctUntilChanged()
        .flatMapLatest { searchType ->
            if(searchType.isLocal) localSearchResultFlow
            else serverSearchResultFlow
        }
        .cachedIn(viewModelScope)


    init {
        checkDisabledSearching()
        listenHistories()
        listenInsertHistory()
        listenData()
    }

    protected abstract fun getLocalSearchResultFlow(
        query: String,
        languageEnum: LanguageEnum,
        pageSize: Int
    ): Flow<PagingData<T>>

    protected abstract suspend fun getServerSearchResultFlow(
        query: String,
        languageEnum: LanguageEnum,
        localPageSize: Int,
        responsePageSize: Int
    ): DefaultResult<Flow<PagingData<T>>>


    open fun onAction(action: CategorySearchAction){
        when(action){
            is CategorySearchAction.SearchQuery -> {
                _state.value.queryState.setTextAndPlaceCursorAtEnd(action.query)
            }
            is CategorySearchAction.DeleteHistory -> {
                viewModelScope.launch {
                    historyRepo.deleteHistoryById(action.history.id ?: 0)
                }
            }
            is CategorySearchAction.InsertHistoryFromQuery -> {
                viewModelScope.launch {
                    insertHistory(_state.value.queryState.text.toString())
                }
            }

            is CategorySearchAction.SelectSearchType -> {
                _state.update { it.copy(
                    searchType = action.searchType
                ) }
            }

            CategorySearchAction.SearchRemote -> {
                if(_state.value.remainingSearchableCount <= 0){
                    _state.update { it.copy(
                        dialogEvent = CategorySearchDialogEvent.ShowAdRequired
                    ) }
                    return
                }
                serverSearchedQueryFlow.value = ""
                serverSearchedQueryFlow.value = _state.value.queryState.text.toString()
            }

            CategorySearchAction.ClearMessage -> {
                _state.update { it.copy(message = null) }
            }

            CategorySearchAction.AdShowedSuccess -> {
                viewModelScope.launch {
                    searchAdRepo.resetCategoryAd()
                    serverSearchedQueryFlow.value = _state.value.queryState.text.toString()
                }
            }
            is CategorySearchAction.ShowDialog -> {
                _state.update { it.copy(dialogEvent = action.dialogEvent) }
            }
        }
    }

    private fun listenData(){
        searchAdRepo
            .remainingCategoryAdCount
            .onEach { remainingCategoryAdCount->
                _state.update { it.copy(remainingSearchableCount = remainingCategoryAdCount) }
            }
            .launchIn(viewModelScope)
    }

    private fun listenHistories(){
        translationRepo
            .getFlowLanguage()
            .flatMapLatest { language ->
                _state.update { it.copy(
                    historyLoading = true
                ) }
                historyRepo
                    .getHistoryFlow(
                        type = historyType,
                        language = language
                    )
            }
            .onEach { histories ->
                _state.update { it.copy(
                    histories = histories,
                    historyLoading = false
                ) }
            }
            .launchIn(viewModelScope)
    }

    private fun listenInsertHistory(){
        _state
            .filter { it.searchType.isLocal }
            .distinctUntilChanged()
            .combine(queryTextFlow){_, query -> query.trim()}
            .debounce(3000L)
            .distinctUntilChanged()
            .onEach { query ->
                insertHistory(query)
            }
            .launchIn(viewModelScope)

        serverSearchedQueryFlow
            .filter { it.isNotBlank() && it.length >= 2 }
            .distinctUntilChanged()
            .onEach { query ->
                insertHistory(query)
            }
            .launchIn(viewModelScope)
    }

    private suspend fun insertHistory(content: String){
        if(content.isBlank() || content.length < 2) return
        historyRepo.upsertHistory(
            content = content.trim(),
            type = historyType,
            language = translationRepo.getLanguage()
        )
    }

    private fun checkDisabledSearching(){
        var serverSearchingEnabled = true
        if(contentType.isCategory && categoryType == CategoryType.Habitat){
            serverSearchingEnabled = false
        }
        if(categoryType == CategoryType.List){
            serverSearchingEnabled = false
        }
        _state.update { it.copy(
            serverSearchingEnabled = serverSearchingEnabled,
            searchType = if(serverSearchingEnabled) it.searchType else SearchType.Local
        ) }
    }
}