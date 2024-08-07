package com.masterplus.animals.features.settings.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.animals.features.settings.presentation.SettingsPageRoot
import kotlinx.serialization.Serializable

@Serializable
private data object SettingsRoute

fun NavController.navigateToSettings(){
    navigate(SettingsRoute)
}

fun NavGraphBuilder.settings(
    onNavigateBack: () -> Unit,
    onNavigateToLinkedAccounts: () -> Unit
){
    composable<SettingsRoute> {
        SettingsPageRoot(
            onNavigateBack = onNavigateBack,
            onNavigateToLinkedAccounts = onNavigateToLinkedAccounts
        )
    }
}