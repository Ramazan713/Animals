package com.masterplus.animals.features.settings.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.animals.features.settings.presentation.link_accounts.LinkAccountsPageRoot
import kotlinx.serialization.Serializable

@Serializable
private data object LinkAccountsRoute

fun NavController.navigateToLinkAccounts(){
    navigate(LinkAccountsRoute)
}

fun NavGraphBuilder.linkAccounts(
    onNavigateBack: () -> Unit
){
    composable<LinkAccountsRoute> {
        LinkAccountsPageRoot(
            onNavigateBack = onNavigateBack
        )
    }
}