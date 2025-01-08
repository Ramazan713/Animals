package com.masterplus.animals.features.search.presentation.app_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.masterplus.animals.core.data.mapper.toCategoryData
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.shared_features.translation.domain.repo.TranslationRepo
import com.masterplus.animals.features.search.domain.enums.HistoryType
import com.masterplus.animals.features.search.domain.repo.HistoryRepo
import com.masterplus.animals.features.search.domain.repo.SearchAdRepo
import com.masterplus.animals.features.search.domain.repo.SearchRemoteRepo
import com.masterplus.animals.features.search.domain.repo.SearchRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class AppSearchViewModel(
    private val historyRepo: HistoryRepo,
    private val translationRepo: TranslationRepo,
    private val searchAdRepo: SearchAdRepo,
    private val searchRepo: SearchRepo,
    private val searchRemoteRepo: SearchRemoteRepo
): ViewModel(){

    private val serverSearchedQueryFlow = MutableStateFlow("")


    private val _state = MutableStateFlow(AppSearchState())
    val state get() = _state.asStateFlow()

    private val queryBaseFlow = _state
        .map { it.query }
        .debounce(300L)
        .filter { it.isNotBlank() }
        .distinctUntilChanged()

    private val localBaseSearchQueryFlow = combine(
        queryBaseFlow,
        translationRepo.getFlowLanguage()
    ){query, language ->
        Pair(query, language)
    }.distinctUntilChanged()

    val classResultsPaging = localBaseSearchQueryFlow
        .flatMapLatest { pair ->
            searchRepo.searchCategory(query = pair.first, language = pair.second, categoryType = CategoryType.Class)
        }
        .cachedIn(viewModelScope)

    val orderResultsPaging = localBaseSearchQueryFlow
        .flatMapLatest { pair ->
            searchRepo.searchCategory(query = pair.first, language = pair.second, categoryType = CategoryType.Order)
        }
        .cachedIn(viewModelScope)

    val familyResultsPaging = localBaseSearchQueryFlow
        .flatMapLatest { pair ->
            searchRepo.searchCategory(query = pair.first, language = pair.second, categoryType = CategoryType.Family)
        }
        .cachedIn(viewModelScope)

    val speciesResultsPaging = localBaseSearchQueryFlow
        .flatMapLatest { pair ->
            searchRepo.searchSpecies(query = pair.first, language = pair.second)
        }
        .map { items -> items.map { it.toCategoryData() } }
        .cachedIn(viewModelScope)




    init {
        listenHistories()
        listenInsertHistory()
        listenData()
    }

    fun onAction(action: AppSearchAction){
        when(action){
            is AppSearchAction.SearchQuery -> {
                _state.update { it.copy(
                    query = action.query
                ) }
            }
            is AppSearchAction.DeleteHistory -> {
                viewModelScope.launch {
                    historyRepo.deleteHistoryById(action.history.id ?: 0)
                }
            }
            is AppSearchAction.InsertHistory -> {
                viewModelScope.launch {
                    insertHistory(action.content)
                }
            }

            is AppSearchAction.SelectSearchType -> {
                _state.update { it.copy(
                    searchType = action.searchType
                ) }
            }

            AppSearchAction.SearchRemote -> {
                if(_state.value.remainingSearchableCount <= 0){
                    _state.update { it.copy(
                        dialogEvent = AppSearchDialogEvent.ShowAdRequired
                    ) }
                    return
                }
                serverSearchedQueryFlow.value = _state.value.query
            }

            AppSearchAction.ClearMessage -> {
                _state.update { it.copy(message = null) }
            }

            AppSearchAction.AdShowedSuccess -> {
                viewModelScope.launch {
                    searchAdRepo.resetAppAd()
                    serverSearchedQueryFlow.value = _state.value.query
                }
            }
            is AppSearchAction.ShowDialog -> {
                _state.update { it.copy(dialogEvent = action.dialogEvent) }
            }
        }
    }

    private fun listenData(){
        searchAdRepo
            .remainingAppAdCount
            .onEach { remainingAppAdCount->
                _state.update { it.copy(remainingSearchableCount = remainingAppAdCount) }
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
                        type = HistoryType.App,
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
            type = HistoryType.App,
            language = translationRepo.getLanguage()
        )
    }

}
