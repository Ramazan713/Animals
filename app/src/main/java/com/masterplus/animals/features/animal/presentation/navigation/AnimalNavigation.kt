package com.masterplus.animals.features.animal.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.animals.features.animal.presentation.AnimalPageRoot
import kotlinx.serialization.Serializable

@Serializable
data object AnimalRoute

fun NavGraphBuilder.animal(){
    composable<AnimalRoute> {
        AnimalPageRoot()
    }
}