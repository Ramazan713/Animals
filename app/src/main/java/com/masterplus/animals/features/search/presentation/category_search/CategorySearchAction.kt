package com.masterplus.animals.features.search.presentation.category_search

import com.masterplus.animals.features.search.domain.enums.SearchType
import com.masterplus.animals.features.search.domain.models.History

sealed interface CategorySearchAction {

    data object SearchRemote: CategorySearchAction

    data class SearchQuery(val query: String): CategorySearchAction

    data object InsertHistoryFromQuery: CategorySearchAction

    data class DeleteHistory(val history: History): CategorySearchAction

    data class SelectSearchType(val searchType: SearchType): CategorySearchAction

    data class ShowDialog(val dialogEvent: CategorySearchDialogEvent? = null): CategorySearchAction

    data object AdShowedSuccess: CategorySearchAction

    data object ClearMessage: CategorySearchAction
}