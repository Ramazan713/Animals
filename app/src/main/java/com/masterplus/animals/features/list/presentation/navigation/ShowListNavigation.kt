package com.masterplus.animals.features.list.presentation.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.animals.features.list.presentation.show_list.ShowListPageRoot
import kotlinx.serialization.Serializable


@Serializable
object ShowListRoute

@ExperimentalMaterial3Api
fun NavGraphBuilder.showList(
    onNavigateToArchive: () -> Unit,
    onNavigateToDetailList: (listId: Int) -> Unit,
    onNavigateToSettings: ()-> Unit,
){
    composable<ShowListRoute>(){
        ShowListPageRoot(
            onNavigateToArchive = onNavigateToArchive,
            onNavigateToDetailList = onNavigateToDetailList,
            onNavigateToSettings = onNavigateToSettings
        )
    }
}