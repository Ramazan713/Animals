package com.masterplus.animals.core.shared_features.savepoint.presentation.auto_savepoint

sealed interface AutoSavePointEvent {
    data class LoadItemPos(
        val pos: Int?
    ): AutoSavePointEvent

    data object RetryPagingAfterAd: AutoSavePointEvent

    data object ShowAd: AutoSavePointEvent
}