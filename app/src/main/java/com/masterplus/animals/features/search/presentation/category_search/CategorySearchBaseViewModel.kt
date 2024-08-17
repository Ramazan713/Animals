package com.masterplus.animals.features.search.presentation.category_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.masterplus.animals.core.shared_features.translation.domain.repo.TranslationRepo
import com.masterplus.animals.features.search.domain.enums.HistoryType
import com.masterplus.animals.features.search.domain.repo.HistoryRepo
import kotlinx.coroutines.delay
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

abstract class CategorySearchBaseViewModel(
    private val historyRepo: HistoryRepo,
    private val translationRepo: TranslationRepo
): ViewModel() {

    abstract val historyType: HistoryType

    protected val _state = MutableStateFlow(CategorySearchState())
    val state = _state.asStateFlow()

    init {
        _state.map { it.query.trim() }
            .debounce(3000L)
            .filter { it.isNotBlank() && it.length > 2 }
            .distinctUntilChanged()
            .onEach { query ->
                insertHistory(query)
            }
            .launchIn(viewModelScope)

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
        }
    }


    private suspend fun insertHistory(content: String){
        historyRepo.upsertHistory(
            content = content.trim(),
            type = historyType,
            language = translationRepo.getLanguage()
        )
    }
}