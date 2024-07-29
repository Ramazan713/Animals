package com.masterplus.animals.features.bio_list.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.features.bio_list.presentation.BioListPageRoot
import kotlinx.serialization.Serializable

@Serializable
data class BioListRoute(
    val categoryId: Int,
    val itemId: Int
){
    val categoryType get() = CategoryType.fromCatId(categoryId)
    val realItemId get() = if(itemId == 0) null else itemId
}


fun NavController.navigateToBioList(categoryId: Int, itemId: Int?){
    navigate(BioListRoute(categoryId, itemId ?: 0))
}


fun NavGraphBuilder.bioList(
    onNavigateBack: () -> Unit,
    onNavigateToBioDetail: (Int) -> Unit
){
    composable<BioListRoute> {
        BioListPageRoot(
            onNavigateBack = onNavigateBack,
            onNavigateToBioDetail = onNavigateToBioDetail
        )
    }
}