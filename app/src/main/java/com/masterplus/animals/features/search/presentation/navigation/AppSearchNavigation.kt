package com.masterplus.animals.features.search.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.animals.core.shared_features.ad.presentation.AdAction
import com.masterplus.animals.core.shared_features.ad.presentation.AdState
import com.masterplus.animals.features.search.presentation.app_search.AppSearchPageRoot
import kotlinx.serialization.Serializable

@Serializable
data object AppSearchRoute

fun NavController.navigateToAppSearch() {
    navigate(AppSearchRoute)
}

fun NavGraphBuilder.appSearch(
    adState: AdState,
    onAdAction: (AdAction) -> Unit,
) {
    composable<AppSearchRoute> {
        AppSearchPageRoot(
            adState = adState,
            onAdAction = onAdAction
        )
    }
}