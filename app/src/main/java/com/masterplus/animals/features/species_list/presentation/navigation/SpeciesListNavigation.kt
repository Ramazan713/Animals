package com.masterplus.animals.features.species_list.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.features.species_list.presentation.SpeciesListPageRoot
import kotlinx.serialization.Serializable

@Serializable
data class SpeciesListRoute(
    val categoryId: Int,
    val itemId: Int,
    val initPosIndex: Int
){
    val categoryType get() = CategoryType.fromCatId(categoryId)
    val realItemId get() = if(itemId == 0) null else itemId
}


fun NavController.navigateToSpeciesList(categoryId: Int, itemId: Int?, initPosIndex: Int = 0){
    navigate(SpeciesListRoute(categoryId, itemId ?: 0, initPosIndex))
}


fun NavGraphBuilder.speciesList(
    onNavigateBack: () -> Unit,
    onNavigateToSpeciesDetail: (Int) -> Unit
){
    composable<SpeciesListRoute> {
        SpeciesListPageRoot(
            onNavigateBack = onNavigateBack,
            onNavigateToSpeciesDetail = onNavigateToSpeciesDetail
        )
    }
}