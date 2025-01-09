package com.masterplus.animals.features.search.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
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
    onNavigateToCategoryListWithDetail: (CategoryType, KingdomType, Int) -> Unit,
    onNavigateToSpeciesList: (CategoryType, KingdomType, Int?, Int) -> Unit,
    onNavigateToSpeciesDetail: (Int) -> Unit,
) {
    composable<AppSearchRoute> {
        AppSearchPageRoot(
            adState = adState,
            onAdAction = onAdAction,
            onNavigateToSpeciesList = onNavigateToSpeciesList,
            onNavigateToSpeciesDetail = onNavigateToSpeciesDetail,
            onNavigateToCategoryListWithDetail = onNavigateToCategoryListWithDetail
        )
    }
}