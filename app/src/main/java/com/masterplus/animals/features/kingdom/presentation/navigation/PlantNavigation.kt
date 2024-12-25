package com.masterplus.animals.features.kingdom.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.presentation.transition.NavAnimatedVisibilityProvider
import com.masterplus.animals.features.kingdom.presentation.KingdomPageRoot
import com.masterplus.animals.features.kingdom.presentation.PlantViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel


@Serializable
data object PlantRoute

fun NavGraphBuilder.plant(
    onNavigateToCategoryListWithDetail: (CategoryType, ItemId) -> Unit,
    onNavigateToCategoryList: (CategoryType) -> Unit,
    onNavigateToSpeciesList: (CategoryType, Int?, Int) -> Unit,
    onNavigateToShowSavePoints: () -> Unit,
    onNavigateToSpeciesDetail: (Int) -> Unit,
    onNavigateToSettings: () -> Unit
){
    composable<PlantRoute> {
        NavAnimatedVisibilityProvider(
            scope = this
        ){
            KingdomPageRoot(
                viewModel = koinViewModel<PlantViewModel>(),
                onNavigateToCategoryListWithDetail = onNavigateToCategoryListWithDetail,
                onNavigateToCategoryList = onNavigateToCategoryList,
                onNavigateToSpeciesList = onNavigateToSpeciesList,
                onNavigateToShowSavePoints = onNavigateToShowSavePoints,
                onNavigateToSpeciesDetail = onNavigateToSpeciesDetail,
                onNavigateToSettings = onNavigateToSettings
            )
        }
    }
}