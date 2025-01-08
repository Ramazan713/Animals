package com.masterplus.animals.features.search.presentation.category_search

import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.features.search.domain.enums.SearchType
import com.masterplus.animals.features.search.domain.models.History

data class CategorySearchState(
    val searchType: SearchType = SearchType.Local,
    val resultLoading: Boolean = false,
    val historyLoading: Boolean = false,
    val query: String = "",
    val titleForPlaceHolder: UiText? = null,
    val histories: List<History> = emptyList(),
    val message: UiText? = null
)
