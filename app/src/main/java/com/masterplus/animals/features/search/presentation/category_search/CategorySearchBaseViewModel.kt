package com.masterplus.animals.features.search.presentation.category_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import com.masterplus.animals.core.shared_features.translation.domain.repo.TranslationRepo
import com.masterplus.animals.features.search.domain.enums.HistoryType
import com.masterplus.animals.features.search.domain.repo.HistoryRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val translationRepo: TranslationRepo
): ViewModel() {

    abstract val historyType: HistoryType

    private val serverSearchedQueryFlow = MutableStateFlow("")

    protected val _state = MutableStateFlow(CategorySearchState())
    val state = _state.asStateFlow()


    private val localSearchResultFlow = _state
        .map { it.query }
        .debounce(300L)
        .filter { it.isNotBlank() }
        .distinctUntilChanged()
        .flatMapLatest { query ->
            val language = translationRepo.getLanguage()
            getLocalSearchResultFlow(query = query, languageEnum = language, pageSize = 20)
        }
        .cachedIn(viewModelScope)

    private val serverSearchResultFlow = serverSearchedQueryFlow
        .filter { it.isNotBlank() }
        .flatMapLatest { query ->
            _state.update { it.copy(isRemoteSearching = true) }
            val language = translationRepo.getLanguage()
            val response = getServerSearchResultFlow(query = query, languageEnum = language, localPageSize = 20, responsePageSize = 10,)
            response.getFailureError?.let { error ->
                _state.update { it.copy(
                    message = error.text
                ) }
            }
            response.onSuccess {
                _state.update { it.copy(remainingSearchableCount = it.remainingSearchableCount - 1) }
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
        listenHistories()
        listenInsertHistory()
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
                _state.update { it.copy(
                    query = action.query
                ) }
            }
            is CategorySearchAction.DeleteHistory -> {
                viewModelScope.launch {
                    historyRepo.deleteHistoryById(action.history.id ?: 0)
                }
            }
            is CategorySearchAction.InsertHistory -> {
                viewModelScope.launch {
                    insertHistory(action.content)
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
                serverSearchedQueryFlow.value = _state.value.query
            }

            CategorySearchAction.ClearMessage -> {
                _state.update { it.copy(message = null) }
            }

            CategorySearchAction.AdShowedSuccess -> {
                _state.update { it.copy(remainingSearchableCount = 2) }
                serverSearchedQueryFlow.value = _state.value.query
            }
            is CategorySearchAction.ShowDialog -> {
                _state.update { it.copy(dialogEvent = action.dialogEvent) }
            }
        }
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
            .map { it.query.trim() }
            .debounce(3000L)
            .filter { it.isNotBlank() && it.length > 2 }
            .distinctUntilChanged()
            .onEach { query ->
                insertHistory(query)
            }
            .launchIn(viewModelScope)

        serverSearchedQueryFlow
            .filter { it.isNotBlank() && it.length > 2 }
            .distinctUntilChanged()
            .onEach { query ->
                insertHistory(query)
            }
            .launchIn(viewModelScope)
    }

    private suspend fun insertHistory(content: String){
        historyRepo.upsertHistory(
            content = content.trim(),
            type = historyType,
            language = translationRepo.getLanguage()
        )
    }
}