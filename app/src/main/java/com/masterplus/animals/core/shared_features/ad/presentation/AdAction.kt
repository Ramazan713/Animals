package com.masterplus.animals.core.shared_features.ad.presentation

import androidx.navigation.NavDestination

sealed interface AdAction {

    data class OnDestinationChange(
        val currentDestination: NavDestination?
    ): AdAction

    data object ResetCounter: AdAction

    data object ClearUiEvent: AdAction

    data class SetPremiumActive(val premiumActive: Boolean): AdAction
}