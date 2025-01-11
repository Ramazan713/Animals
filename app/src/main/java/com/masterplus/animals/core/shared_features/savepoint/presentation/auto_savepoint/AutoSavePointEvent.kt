package com.masterplus.animals.core.shared_features.savepoint.presentation.auto_savepoint

sealed interface AutoSavePointEvent {
    data class LoadItemPos(
        val pos: Int?
    ): AutoSavePointEvent

    data class LoadRequiredPage(
        val orderKey: Int
    ): AutoSavePointEvent

    data object RetryPaging: AutoSavePointEvent

    data object ShowAd: AutoSavePointEvent
}