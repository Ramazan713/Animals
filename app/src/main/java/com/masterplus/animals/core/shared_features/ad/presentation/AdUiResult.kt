package com.masterplus.animals.core.shared_features.ad.presentation

sealed interface AdUiResult {
    data class OnShowingRewardSuccess(
        val label: String,
    ): AdUiResult
}