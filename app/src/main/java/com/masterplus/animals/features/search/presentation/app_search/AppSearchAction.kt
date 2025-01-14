package com.masterplus.animals.features.search.presentation.app_search

import com.masterplus.animals.features.search.domain.enums.SearchType
import com.masterplus.animals.features.search.domain.models.History

sealed interface AppSearchAction {

    data object SearchRemote: AppSearchAction

    data class SearchQuery(val query: String): AppSearchAction

    data object InsertHistoryFromQuery: AppSearchAction

    data class DeleteHistory(val history: History): AppSearchAction

    data class SelectSearchType(val searchType: SearchType): AppSearchAction

    data class ShowDialog(val dialogEvent: AppSearchDialogEvent? = null): AppSearchAction

    data object AdShowedSuccess: AppSearchAction

    data object ClearMessage: AppSearchAction
}