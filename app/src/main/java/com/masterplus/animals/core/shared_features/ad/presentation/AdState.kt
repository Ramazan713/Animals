package com.masterplus.animals.core.shared_features.ad.presentation

import com.masterplus.animals.core.domain.utils.UiText


data class LoadingRewardAd(
    val isLoading: Boolean = false,
    val label: String = "",
    val error: UiText? = null
)

data class AdState(
    val loadingRewardAd: LoadingRewardAd = LoadingRewardAd(),
    val uiEvent: AdUiEvent? = null,
    val isPremiumActive: Boolean = false,
    val uiResult: AdUiResult? = null
)
