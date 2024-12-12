package com.masterplus.animals.features.settings.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.animals.features.settings.presentation.savepoint_settings.SavePointSettingsPageRoot
import kotlinx.serialization.Serializable

@Serializable
data object SavePointSettingsRoute

fun NavController.navigateToSavePointSettings() {
    navigate(SavePointSettingsRoute)
}

fun NavGraphBuilder.savePointSettings(
    onNavigateBack: () -> Unit
) {
    composable<SavePointSettingsRoute> {
        SavePointSettingsPageRoot(
            onNavigateBack = onNavigateBack,
            showSpeciesSection = true,
            showCategorySection = true,
        )
    }
}


@Serializable
data object SavePointCategorySettingsRoute

fun NavController.navigateToSavePointCategorySettings() {
    navigate(SavePointCategorySettingsRoute)
}

fun NavGraphBuilder.savePointCategorySettings(
    onNavigateBack: () -> Unit
) {
    composable<SavePointCategorySettingsRoute> {
        SavePointSettingsPageRoot(
            onNavigateBack = onNavigateBack,
            showSpeciesSection = false,
            showCategorySection = true,
        )
    }
}


@Serializable
data object SavePointSpeciesSettingsRoute

fun NavController.navigateToSavePointSpeciesSettings() {
    navigate(SavePointSpeciesSettingsRoute)
}

fun NavGraphBuilder.savePointSpeciesSettings(
    onNavigateBack: () -> Unit
) {
    composable<SavePointSpeciesSettingsRoute> {
        SavePointSettingsPageRoot(
            onNavigateBack = onNavigateBack,
            showSpeciesSection = true,
            showCategorySection = false,
        )
    }
}



