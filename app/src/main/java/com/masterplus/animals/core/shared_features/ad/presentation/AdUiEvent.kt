package com.masterplus.animals.core.shared_features.ad.presentation

import com.masterplus.animals.core.domain.enums.ContentType

sealed interface AdUiEvent {

    data object CheckInterstitialAdShowStatus: AdUiEvent

    data object LoadInterstitialAd: AdUiEvent

    data class LoadRewordedAd(
        val contentType: ContentType
    ): AdUiEvent

    data class ShowRewordedAd(
        val contentType: ContentType
    ): AdUiEvent
}