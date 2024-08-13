package com.masterplus.animals.features.species_detail.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.animals.features.species_detail.presentation.SpeciesDetailPageRoot
import kotlinx.serialization.Serializable

@Serializable
data class SpeciesDetailRoute(
    val speciesId: Int
)

fun NavController.navigateToSpeciesDetail(speciesId: Int){
    navigate(SpeciesDetailRoute(speciesId))
}

fun NavGraphBuilder.speciesDetail(
    onNavigateBack: () -> Unit
){
    composable<SpeciesDetailRoute> {
        SpeciesDetailPageRoot(
            onNavigateBack = onNavigateBack
        )
    }
}