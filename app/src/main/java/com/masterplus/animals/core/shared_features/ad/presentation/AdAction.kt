package com.masterplus.animals.core.shared_features.ad.presentation

import androidx.navigation.NavDestination

sealed interface AdAction {

    data class OnDestinationChange(
        val currentDestination: NavDestination?
    ): AdAction

    data class RequestShowRewardAd(
        val label: String
    ): AdAction

    data object OnRewardAdLoading: AdAction
    data object OnRewardAdLoaded: AdAction
    data class OnRewardAdError(val error: String): AdAction
    data class OnSuccessShowingRewardAd(
        val label: String
    ): AdAction

    data object ResetCounter: AdAction



    data object ClearUiEvent: AdAction

    data object ClearUiResult: AdAction

    data class SetPremiumActive(val premiumActive: Boolean): AdAction
}