package com.masterplus.animals.features.search.presentation.category_search

sealed interface CategorySearchAction {
    data class SearchQuery(val query: String): CategorySearchAction
}