package com.masterplus.animals.features.bio_detail.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.animals.features.bio_detail.presentation.BioDetailPageRoot
import kotlinx.serialization.Serializable

@Serializable
data class BioDetailRoute(
    val id: Int
)

fun NavController.navigateToBioDetail(id: Int){
    navigate(BioDetailRoute(id))
}

fun NavGraphBuilder.bioDetail(
    onNavigateBack: () -> Unit
){
    composable<BioDetailRoute> {
        BioDetailPageRoot(
            onNavigateBack = onNavigateBack
        )
    }
}