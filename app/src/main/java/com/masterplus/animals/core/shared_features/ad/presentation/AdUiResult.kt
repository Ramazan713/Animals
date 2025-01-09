package com.masterplus.animals.core.shared_features.ad.presentation

import com.masterplus.animals.core.domain.utils.UiText

sealed interface AdUiResult {
    data class OnShowingRewardSuccess(
        val label: String,
    ): AdUiResult

    data class OnShowingRewardFailed(
        val label: String,
        val error: UiText
    ): AdUiResult
}