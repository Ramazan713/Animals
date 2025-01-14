package com.masterplus.animals.features.search.presentation.app_search

import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.masterplus.animals.core.data.mapper.toCategoryData
import com.masterplus.animals.core.data.utils.RemoteKeyUtil
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.repo.CategoryRepo
import com.masterplus.animals.core.domain.repo.SpeciesRepo
import com.masterplus.animals.core.shared_features.preferences.domain.AppConfigPreferences
import com.masterplus.animals.core.shared_features.translation.domain.repo.TranslationRepo
import com.masterplus.animals.features.search.domain.enums.HistoryType
import com.masterplus.animals.features.search.domain.repo.HistoryRepo
import com.masterplus.animals.features.search.domain.repo.SearchAdRepo
import com.masterplus.animals.features.search.domain.repo.SearchRemoteRepo
import com.masterplus.animals.features.search.domain.repo.SearchRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
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
    private val categoryRepo: CategoryRepo,
    private val speciesRepo: SpeciesRepo,
    private val searchRemoteRepo: SearchRemoteRepo,
    private val appConfigPreferences: AppConfigPreferences
): ViewModel(){

    private val serverSearchedQueryFlow = MutableStateFlow("")


    private val _state = MutableStateFlow(AppSearchState())
    val state get() = _state.asStateFlow()

    private val queryTextFlow = snapshotFlow { _state.value.queryState.text.toString() }
    
    private val searchTypeFlow = _state
        .map { it.searchType }
        .distinctUntilChanged()

    private val queryBaseFlow = _state
        .filter { it.searchType.isLocal }
        .distinctUntilChanged()
        .combine(queryTextFlow){_, query -> query}
        .debounce(300L)
        .filter { it.isNotBlank() }
        .distinctUntilChanged()

    private val queryRemoteKeyFlow = serverSearchedQueryFlow
        .filter { it.isNotBlank() }
        .map { RemoteKeyUtil.getAppSearchKey(it) }
        .distinctUntilChanged()

    private val localPageSizeFlow = appConfigPreferences.dataFlow
        .map { it.pagination.searchAppLocalPageSize }
        .distinctUntilChanged()

    private val responsePageSizeFlow = appConfigPreferences.dataFlow
        .map { it.pagination.searchAppResponsePageSize }
        .distinctUntilChanged()
    

    private val localBaseSearchQueryFlow = combine(
        queryBaseFlow,
        translationRepo.getFlowLanguage(),
        localPageSizeFlow
    ){query, language, localPageSize ->
        Triple(query, language, localPageSize)
    }.distinctUntilChanged()

    private val remoteBaseSearchQueryFlow = combine(
        queryRemoteKeyFlow,
        translationRepo.getFlowLanguage(),
        responsePageSizeFlow
    ){key, language, responsePageSize ->
        Triple(key, language, responsePageSize)
    }.distinctUntilChanged()


    private val classLocalResultFlow = localBaseSearchQueryFlow
        .flatMapLatest { triple ->
            searchRepo.searchCategory(query = triple.first, language = triple.second, categoryType = CategoryType.Class, pageSize = triple.third)
        }.cachedIn(viewModelScope)

    private val classRemoteResultFlow = remoteBaseSearchQueryFlow
        .flatMapLatest { triple ->
            categoryRepo.getLocalPagingClasses(label = triple.first, language = triple.second, pageSize = triple.third)
                .map { items -> items.map { it.toCategoryData() } }
        }.cachedIn(viewModelScope)

    val classResultsPaging = searchTypeFlow
        .flatMapLatest { searchType ->
            if(searchType.isLocal) classLocalResultFlow else classRemoteResultFlow
        }
        .cachedIn(viewModelScope)


    private val orderLocalResultFlow = localBaseSearchQueryFlow
        .flatMapLatest { triple ->
            searchRepo.searchCategory(query = triple.first, language = triple.second, categoryType = CategoryType.Order, pageSize = triple.third)
        }.cachedIn(viewModelScope)

    private val orderRemoteResultFlow = remoteBaseSearchQueryFlow
        .flatMapLatest { triple ->
            categoryRepo.getLocalPagingOrders(label = triple.first, language = triple.second, pageSize = triple.third)
                .map { items -> items.map { it.toCategoryData() } }
        }.cachedIn(viewModelScope)

    val orderResultsPaging = searchTypeFlow
        .flatMapLatest { searchType ->
            if(searchType.isLocal) orderLocalResultFlow else orderRemoteResultFlow
        }
        .cachedIn(viewModelScope)

    private val familyLocalResultFlow = localBaseSearchQueryFlow
        .flatMapLatest { triple ->
            searchRepo.searchCategory(query = triple.first, language = triple.second, categoryType = CategoryType.Family, pageSize = triple.third)
        }.cachedIn(viewModelScope)

    private val familyRemoteResultFlow = remoteBaseSearchQueryFlow
        .flatMapLatest { triple ->
            categoryRepo.getLocalPagingFamilies(label = triple.first, language = triple.second, pageSize = triple.third)
                .map { items -> items.map { it.toCategoryData() } }
        }.cachedIn(viewModelScope)

    val familyResultsPaging = searchTypeFlow
        .flatMapLatest { searchType ->
            if(searchType.isLocal) familyLocalResultFlow else familyRemoteResultFlow
        }
        .cachedIn(viewModelScope)


    private val speciesLocalResultFlow = localBaseSearchQueryFlow
        .flatMapLatest { triple ->
            searchRepo.searchSpecies(query = triple.first, language = triple.second, pageSize = triple.third)
        }.cachedIn(viewModelScope)

    private val speciesRemoteResultFlow = remoteBaseSearchQueryFlow
        .flatMapLatest { triple ->
            speciesRepo.getLocalPagingSpecies(label = triple.first, language = triple.second, pageSize = triple.third)
        }.cachedIn(viewModelScope)

    val speciesResultsPaging = searchTypeFlow
        .flatMapLatest { searchType ->
            if(searchType.isLocal) speciesLocalResultFlow else speciesRemoteResultFlow
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
                _state.value.queryState.setTextAndPlaceCursorAtEnd(action.query)
            }
            is AppSearchAction.DeleteHistory -> {
                viewModelScope.launch {
                    historyRepo.deleteHistoryById(action.history.id ?: 0)
                }
            }
            is AppSearchAction.InsertHistoryFromQuery -> {
                viewModelScope.launch {
                    insertHistory(_state.value.queryState.text.toString())
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
                serverSearchedQueryFlow.value = ""
                serverSearchedQueryFlow.value = _state.value.queryState.text.toString()
            }

            AppSearchAction.ClearMessage -> {
                _state.update { it.copy(message = null) }
            }

            AppSearchAction.AdShowedSuccess -> {
                viewModelScope.launch {
                    searchAdRepo.resetAppAd()
                    serverSearchedQueryFlow.value = _state.value.queryState.text.toString()
                }
            }
            is AppSearchAction.ShowDialog -> {
                _state.update { it.copy(dialogEvent = action.dialogEvent) }
            }
        }
    }

    private fun listenData(){
        combine(
            serverSearchedQueryFlow
                .filter { it.isNotBlank() },
            translationRepo.getFlowLanguage(),
            responsePageSizeFlow
        ){ query, language, responsePageSize ->
            Triple(query, language, responsePageSize)
        }.onEach { triple ->
            _state.update { it.copy(isRemoteSearching = true) }
            val response = searchRemoteRepo.searchAll(
                query = triple.first,
                responsePageSize = triple.third,
                languageEnum = triple.second
            )
            response.onFailure { error ->
                _state.update { it.copy(message = error.text) }
            }
            response.onSuccessAsync {
                searchAdRepo.decreaseAppAd()
            }
            _state.update { it.copy(isRemoteSearching = false) }
        }
        .launchIn(viewModelScope)

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

    @OptIn(FlowPreview::class)
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
            type = HistoryType.App,
            language = translationRepo.getLanguage()
        )
    }

}
