package com.masterplus.animals.features.list.presentation.archive_list.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.animals.features.list.presentation.archive_list.ArchiveListPageRoot
import kotlinx.serialization.Serializable


@Serializable
private data object ArchiveListRoute

fun NavController.navigateToArchiveList(){
    navigate(ArchiveListRoute)
}

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@ExperimentalMaterial3Api
fun NavGraphBuilder.archiveList(
    onNavigateBack: () -> Unit,
    onNavigateToDetailList: (listId: Int)->Unit,
){
    composable<ArchiveListRoute>{
        ArchiveListPageRoot(
            onNavigateBack = onNavigateBack,
            onNavigateToDetailList = onNavigateToDetailList
        )
    }
}