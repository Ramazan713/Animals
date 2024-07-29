package com.masterplus.animals.features.app.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.masterplus.animals.features.animal.presentation.navigation.AnimalRoute
import com.masterplus.animals.features.animal.presentation.navigation.animal
import com.masterplus.animals.features.app.presentation.extensions.navigateToBar
import com.masterplus.animals.features.app.presentation.model.kBottomBarRoutes
import com.masterplus.animals.features.bio_list.presentation.navigation.bioList
import com.masterplus.animals.features.bio_list.presentation.navigation.navigateToBioList
import com.masterplus.animals.features.category_list.presentation.navigation.categoryList
import com.masterplus.animals.features.category_list.presentation.navigation.categoryListWithDetail
import com.masterplus.animals.features.category_list.presentation.navigation.navigateToCategoryList
import com.masterplus.animals.features.category_list.presentation.navigation.navigateToCategoryListWithDetail
import com.masterplus.animals.features.settings.presentation.navigation.settings

@Composable
fun MyApp(
    navHostController: NavHostController = rememberNavController()
) {

    val backStackEntry by navHostController.currentBackStackEntryAsState()

    val currentAppNavRoute by remember(backStackEntry) {
        derivedStateOf {
            val route = navHostController.currentDestination?.route?.split(".")?.lastOrNull()
            kBottomBarRoutes.find { it.route.toString().contains(route ?: "") }
        }
    }

    val showNavigationBar by remember(currentAppNavRoute) {
        derivedStateOf {
            currentAppNavRoute != null
        }
    }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(visible = showNavigationBar) {
                BottomAppBar {
                    kBottomBarRoutes.forEach { barItem ->
                        val selected = currentAppNavRoute == barItem
                        NavigationBarItem(
                            selected = selected,
                            label = {
                                Text(text = barItem.title)
                            },
                            onClick = {
                                navHostController.navigateToBar(barItem)
                            },
                            icon = {
                                Icon(
                                    imageVector = barItem.getCurrentIconVector(selected),
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }
            }
        }
    ) { paddings ->
        NavHost(
            modifier = Modifier
                .padding(paddings),
            navController = navHostController,
            startDestination = AnimalRoute
        ){
            animal(
                onNavigateToCategoryList = { categoryType ->
                    navHostController.navigateToCategoryList(categoryType)
                },
                onNavigateToCategoryListWithDetail = { categoryType, itemId ->
                    navHostController.navigateToCategoryListWithDetail(categoryType, itemId)
                },
                onNavigateToBioList = { categoryType, itemId ->
                    navHostController.navigateToBioList(categoryType.catId, itemId)
                },
            )

            settings()

            categoryList(
                onNavigateBack = {
                    navHostController.navigateUp()
                },
                onNavigateToBioList = { categoryType, itemId ->
                    navHostController.navigateToBioList(categoryType.catId, itemId)
                },
                onNavigateToCategoryListWithDetail = { categoryType, itemId ->
                    navHostController.navigateToCategoryListWithDetail(categoryType, itemId)
                },
            )

            categoryListWithDetail(
                onNavigateBack = {
                    navHostController.navigateUp()
                },
                onNavigateToBioList = { categoryType, itemId ->
                    navHostController.navigateToBioList(categoryType.catId, itemId)
                },
                onNavigateToCategoryListWithDetail = { categoryType, itemId ->
                    navHostController.navigateToCategoryListWithDetail(categoryType, itemId)
                },
            )

            bioList(
                onNavigateBack = {
                    navHostController.navigateUp()
                },
                onNavigateToBioDetail = { itemId ->

                }
            )
        }
    }
}


