package com.masterplus.animals.core.shared_features.ad.presentation

data class AdState(
    val isLoading: Boolean = false,
    val uiEvent: AdUiEvent? = null,
    val isPremiumActive: Boolean = false
)
