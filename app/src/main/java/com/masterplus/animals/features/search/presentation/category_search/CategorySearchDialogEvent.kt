package com.masterplus.animals.features.search.presentation.category_search

sealed interface CategorySearchDialogEvent {

    data object ShowAdRequired: CategorySearchDialogEvent
}