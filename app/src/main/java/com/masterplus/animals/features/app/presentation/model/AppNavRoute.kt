package com.masterplus.animals.features.app.presentation.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.masterplus.animals.features.animal.presentation.navigation.AnimalRoute
import com.masterplus.animals.features.settings.presentation.navigation.SettingsRoute

sealed class AppNavRoute(
    val route: Any,
    val selectedIconVector: ImageVector,
    val unSelectedIconVector: ImageVector,
    val title: String
) {

    data object Animal: AppNavRoute(
        route = AnimalRoute,
        selectedIconVector = Icons.Filled.Home,
        unSelectedIconVector = Icons.Outlined.Home,
        title = "Animal"
    )

    data object Settings: AppNavRoute(
        route = SettingsRoute,
        selectedIconVector = Icons.Filled.Settings,
        unSelectedIconVector = Icons.Outlined.Settings,
        title = "Settings"
    )


    fun getCurrentIconVector(selected: Boolean): ImageVector{
        return if(selected) selectedIconVector else unSelectedIconVector
    }

}

val kBottomBarRoutes = listOf(
    AppNavRoute.Animal, AppNavRoute.Settings
)