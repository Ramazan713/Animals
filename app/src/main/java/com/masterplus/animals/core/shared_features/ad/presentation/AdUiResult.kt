package com.masterplus.animals.core.shared_features.ad.presentation

sealed interface AdUiResult {
    data object OnShowingRewardSuccess: AdUiResult
}