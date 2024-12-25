package com.masterplus.animals.features.kingdom.presentation.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.presentation.transition.NavAnimatedVisibilityProvider
import com.masterplus.animals.features.kingdom.presentation.AnimalViewModel
import com.masterplus.animals.features.kingdom.presentation.KingdomPageRoot
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel


typealias ItemId = Int

@Serializable
data object AnimalRoute

fun NavGraphBuilder.animal(
    onNavigateToCategoryListWithDetail: (CategoryType, ItemId) -> Unit,
    onNavigateToCategoryList: (CategoryType) -> Unit,
    onNavigateToSpeciesList: (CategoryType, Int?, Int) -> Unit,
    onNavigateToShowSavePoints: () -> Unit,
    onNavigateToSpeciesDetail: (Int) -> Unit,
    onNavigateToSettings: () -> Unit,
){
    composable<AnimalRoute> {
        NavAnimatedVisibilityProvider(
            scope = this
        ){
            KingdomPageRoot(
                viewModel = koinViewModel<AnimalViewModel>(),
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