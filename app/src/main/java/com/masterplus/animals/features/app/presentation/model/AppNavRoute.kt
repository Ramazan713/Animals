package com.masterplus.animals.features.app.presentation.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.automirrored.outlined.ViewList
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalFlorist
import androidx.compose.ui.graphics.vector.ImageVector
import com.masterplus.animals.features.kingdom.presentation.navigation.AnimalRoute
import com.masterplus.animals.features.kingdom.presentation.navigation.PlantRoute
import com.masterplus.animals.features.list.presentation.navigation.ShowListRoute

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

    data object Plant: AppNavRoute(
        route = PlantRoute,
        selectedIconVector = Icons.Filled.LocalFlorist,
        unSelectedIconVector = Icons.Outlined.LocalFlorist,
        title = "Plant"
    )

    data object Lists: AppNavRoute(
        route = ShowListRoute,
        selectedIconVector = Icons.AutoMirrored.Filled.ViewList,
        unSelectedIconVector = Icons.AutoMirrored.Outlined.ViewList,
        title = "Lists"
    )


    fun getCurrentIconVector(selected: Boolean): ImageVector{
        return if(selected) selectedIconVector else unSelectedIconVector
    }

}

val x = listOf(PlantRoute, ShowListRoute)

val kBottomBarRoutes = listOf(
   AppNavRoute.Animal,  AppNavRoute.Plant, AppNavRoute.Lists
)