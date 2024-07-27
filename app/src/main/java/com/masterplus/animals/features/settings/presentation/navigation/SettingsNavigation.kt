package com.masterplus.animals.features.settings.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.animals.features.settings.presentation.SettingsPage
import kotlinx.serialization.Serializable

@Serializable
data object SettingsRoute

fun NavGraphBuilder.settings(){
    composable<SettingsRoute> {
        SettingsPage()
    }
}