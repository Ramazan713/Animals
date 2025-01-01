package com.masterplus.animals.core.shared_features.ad.presentation

import androidx.navigation.NavDestination
import com.masterplus.animals.core.domain.enums.ContentType

sealed interface AdAction {

    data class OnDestinationChange(
        val currentDestination: NavDestination?
    ): AdAction

    data class RequestShowRewardAd(
        val contentType: ContentType,
    ): AdAction

    data object ResetCounter: AdAction

    data class ResetReadCounter(
        val contentType: ContentType
    ): AdAction

    data object ClearUiEvent: AdAction

    data object ClearUiResult: AdAction

    data class SetPremiumActive(val premiumActive: Boolean): AdAction
}