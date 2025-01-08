package com.masterplus.animals.features.search.presentation.app_search

sealed interface AppSearchDialogEvent {

    data object ShowAdRequired: AppSearchDialogEvent
}