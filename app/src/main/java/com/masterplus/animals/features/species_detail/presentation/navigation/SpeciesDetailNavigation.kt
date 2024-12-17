package com.masterplus.animals.features.species_detail.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.animals.core.presentation.transition.NavAnimatedVisibilityProvider
import com.masterplus.animals.features.species_detail.presentation.SpeciesDetailPageRoot
import kotlinx.serialization.Serializable

@Serializable
data class SpeciesDetailRoute(
    val speciesId: Int,
    val listIdControl: Int?
)

fun NavController.navigateToSpeciesDetail(speciesId: Int, listIdControl: Int? = null){
    navigate(SpeciesDetailRoute(speciesId, listIdControl))
}

fun NavGraphBuilder.speciesDetail(
    onNavigateBack: () -> Unit
){
    composable<SpeciesDetailRoute> {
        NavAnimatedVisibilityProvider(
            scope = this
        ){
            SpeciesDetailPageRoot(
                onNavigateBack = onNavigateBack
            )
        }
    }
}