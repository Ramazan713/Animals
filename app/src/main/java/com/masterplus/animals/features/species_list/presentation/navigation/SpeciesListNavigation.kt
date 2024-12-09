package com.masterplus.animals.features.species_list.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.ContentType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.presentation.transition.NavAnimatedVisibilityProvider
import com.masterplus.animals.features.species_list.presentation.SpeciesListPageRoot
import kotlinx.serialization.Serializable

@Serializable
data class SpeciesListRoute(
    val categoryId: Int,
    val itemId: Int,
    val initPosIndex: Int,
    val kingdomId: Int
){
    val categoryType get() = CategoryType.fromCatId(categoryId)
    val kingdomType: KingdomType get() = KingdomType.fromKingdomId(kingdomId)
    val realItemId get() = if(itemId == 0) null else itemId
}


fun NavController.navigateToSpeciesList(categoryId: Int, itemId: Int?, kingdomType: KingdomType? = null, initPosIndex: Int = 0){
    navigate(SpeciesListRoute(
        categoryId = categoryId,
        itemId = itemId ?: 0,
        initPosIndex = initPosIndex,
        kingdomId = kingdomType?.kingdomId ?: KingdomType.DEFAULT.kingdomId
    ))
}


fun NavGraphBuilder.speciesList(
    onNavigateBack: () -> Unit,
    onNavigateToSpeciesDetail: (Int) -> Unit,
    onNavigateToCategorySearch: (CategoryType, ContentType, Int?) -> Unit
){
    composable<SpeciesListRoute> {
        NavAnimatedVisibilityProvider(
            scope = this
        ){
            SpeciesListPageRoot(
                onNavigateBack = onNavigateBack,
                onNavigateToSpeciesDetail = onNavigateToSpeciesDetail,
                onNavigateToCategorySearch = onNavigateToCategorySearch
            )
        }
    }
}