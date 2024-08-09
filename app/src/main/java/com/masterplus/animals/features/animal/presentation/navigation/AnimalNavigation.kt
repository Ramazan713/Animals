package com.masterplus.animals.features.animal.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.features.animal.presentation.AnimalPageRoot
import kotlinx.serialization.Serializable


typealias ItemId = Int

@Serializable
data object AnimalRoute

fun NavGraphBuilder.animal(
    onNavigateToCategoryListWithDetail: (CategoryType, ItemId) -> Unit,
    onNavigateToCategoryList: (CategoryType) -> Unit,
    onNavigateToSpeciesList: (CategoryType, Int?, Int) -> Unit,
    onNavigateToShowSavePoints: () -> Unit,
    onNavigateToSpeciesDetail: (Int) -> Unit,
    onNavigateToSettings: () -> Unit
){
    composable<AnimalRoute> {
        AnimalPageRoot(
            onNavigateToCategoryListWithDetail = onNavigateToCategoryListWithDetail,
            onNavigateToCategoryList = onNavigateToCategoryList,
            onNavigateToSpeciesList = onNavigateToSpeciesList,
            onNavigateToShowSavePoints = onNavigateToShowSavePoints,
            onNavigateToSpeciesDetail = onNavigateToSpeciesDetail,
            onNavigateToSettings = onNavigateToSettings
        )
    }
}