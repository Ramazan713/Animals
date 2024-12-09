package com.masterplus.animals.features.app.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.masterplus.animals.core.presentation.transition.LocalSharedTransitionScope
import com.masterplus.animals.features.app.presentation.extensions.navigateToBar
import com.masterplus.animals.features.app.presentation.model.kBottomBarRoutes


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class
)
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
        SharedTransitionLayout {
            CompositionLocalProvider(
                LocalSharedTransitionScope provides this,
            ) {
                AppNavHost(
                    navHostController = navHostController,
                    modifier = Modifier.padding(paddings)
                )
            }
        }
    }
}


