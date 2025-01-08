package com.masterplus.animals.core.shared_features.ad.presentation

sealed interface AdUiEvent {

    data object CheckInterstitialAdShowStatus: AdUiEvent

    data object LoadInterstitialAd: AdUiEvent

    data class LoadRewordedAd(
        val label: String
    ): AdUiEvent

    data class ShowRewordedAd(
        val label: String
    ): AdUiEvent
}