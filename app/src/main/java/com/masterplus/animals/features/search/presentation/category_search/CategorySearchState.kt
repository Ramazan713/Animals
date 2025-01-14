package com.masterplus.animals.features.search.presentation.category_search

import androidx.compose.foundation.text.input.TextFieldState
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.features.search.domain.enums.SearchType
import com.masterplus.animals.features.search.domain.models.History

data class CategorySearchState(
    val searchType: SearchType = SearchType.Local,
    val remainingSearchableCount: Int = 0,
    val isRemoteSearching: Boolean = false,
    val resultLoading: Boolean = false,
    val historyLoading: Boolean = false,
    val queryState: TextFieldState = TextFieldState(""),
    val titleForPlaceHolder: UiText? = null,
    val histories: List<History> = emptyList(),
    val message: UiText? = null,
    val dialogEvent: CategorySearchDialogEvent? = null,
    val adLabel: String = "CategorySearchAdLabel"
)
