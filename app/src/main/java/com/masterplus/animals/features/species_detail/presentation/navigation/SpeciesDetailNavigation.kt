package com.masterplus.animals.features.species_detail.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.animals.features.species_detail.presentation.SpeciesDetailPageRoot
import kotlinx.serialization.Serializable

@Serializable
data class SpeciesDetailRoute(
    val id: Int
)

fun NavController.navigateToSpeciesDetail(id: Int){
    navigate(SpeciesDetailRoute(id))
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