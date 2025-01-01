package com.masterplus.animals.features.app.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.masterplus.animals.core.presentation.transition.LocalSharedTransitionScope
import com.masterplus.animals.core.shared_features.ad.presentation.AdAction
import com.masterplus.animals.core.shared_features.ad.presentation.AdMobHandler
import com.masterplus.animals.core.shared_features.ad.presentation.AdViewModel
import com.masterplus.animals.features.app.presentation.components.NoConnectionText
import com.masterplus.animals.features.app.presentation.extensions.navigateToBar
import com.masterplus.animals.features.app.presentation.model.kBottomBarRoutes
import org.koin.androidx.compose.koinViewModel


@OptIn(
    ExperimentalSharedTransitionApi::class
)
@Composable
fun MyApp(
    viewModel: AppViewModel = koinViewModel(),
    adViewModel: AdViewModel = koinViewModel(),
    navHostController: NavHostController = rememberNavController()
) {

    val appState by viewModel.state.collectAsStateWithLifecycle()
    val adState by adViewModel.state.collectAsStateWithLifecycle()

    val backStackEntry by navHostController.currentBackStackEntryAsState()

    val currentDestination by remember(backStackEntry) {
        derivedStateOf {
            backStackEntry?.destination
        }
    }

    val currentAppNavRoute by remember(currentDestination) {
        derivedStateOf {
            kBottomBarRoutes.find { barRoute ->
                currentDestination?.hierarchy?.any { it.hasRoute(barRoute.route::class) } != false
            }
        }
    }

    val showNavigationBar by remember(currentAppNavRoute) {
        derivedStateOf {
            currentAppNavRoute != null
        }
    }

    LaunchedEffect(currentDestination){
        adViewModel.onAction(AdAction.OnDestinationChange(currentDestination))
    }

    AdMobHandler(
        adUiEventState = adState.uiEvent,
        onAdAction = adViewModel::onAction
    )


    Column {
        Scaffold(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
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
            },
        ) { paddings ->
            SharedTransitionLayout {
                CompositionLocalProvider(
                    LocalSharedTransitionScope provides this,
                ) {
                    AppNavHost(
                        navHostController = navHostController,
                        modifier = Modifier.padding(paddings),
                        onAdAction = adViewModel::onAction,
                        adUiResult = adState.uiResult
                    )
                }
            }
        }
        AnimatedVisibility(
            !appState.isNetworkConnected
        ) {
            NoConnectionText(
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}