package com.masterplus.animals.features.plant.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.features.animal.presentation.navigation.ItemId
import com.masterplus.animals.features.plant.presentation.PlantPageRoot
import kotlinx.serialization.Serializable


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
        PlantPageRoot(
            onNavigateToCategoryListWithDetail = onNavigateToCategoryListWithDetail,
            onNavigateToCategoryList = onNavigateToCategoryList,
            onNavigateToSpeciesList = onNavigateToSpeciesList,
            onNavigateToShowSavePoints = onNavigateToShowSavePoints,
            onNavigateToSpeciesDetail = onNavigateToSpeciesDetail,
            onNavigateToSettings = onNavigateToSettings
        )
    }
}