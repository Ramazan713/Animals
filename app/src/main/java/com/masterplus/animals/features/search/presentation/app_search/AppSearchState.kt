package com.masterplus.animals.features.search.presentation.app_search

import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.presentation.models.CategoryDataRowModel
import com.masterplus.animals.features.search.domain.enums.SearchType
import com.masterplus.animals.features.search.domain.models.History

data class AppSearchState(
    val searchType: SearchType = SearchType.Local,
    val remainingSearchableCount: Int = 0,
    val isRemoteSearching: Boolean = false,
    val historyLoading: Boolean = false,
    val query: String = "",
    val histories: List<History> = emptyList(),
    val message: UiText? = null,
    val dialogEvent: AppSearchDialogEvent? = null,
    val adLabel: String = "AppSearchAdLabel",
    val classes: CategoryDataRowModel = CategoryDataRowModel(),
    val orders: CategoryDataRowModel = CategoryDataRowModel(),
    val families: CategoryDataRowModel = CategoryDataRowModel(),
    val species: CategoryDataRowModel = CategoryDataRowModel(),
)