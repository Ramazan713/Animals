package com.masterplus.animals.features.search.presentation.category_search

import com.masterplus.animals.features.search.domain.models.History

sealed interface CategorySearchAction {
    data class SearchQuery(val query: String): CategorySearchAction

    data class InsertHistory(val content: String): CategorySearchAction

    data class DeleteHistory(val history: History): CategorySearchAction
}