package com.masterplus.animals.features.search.presentation.category_search

import com.masterplus.animals.core.domain.utils.UiText

data class CategorySearchState(
    val isLoading: Boolean = false,
    val query: String = "",
    val titleForPlaceHolder: UiText? = null
)
