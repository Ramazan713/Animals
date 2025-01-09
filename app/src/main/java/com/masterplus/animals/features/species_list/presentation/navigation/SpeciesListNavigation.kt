package com.masterplus.animals.features.species_list.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.ContentType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.presentation.transition.NavAnimatedVisibilityProvider
import com.masterplus.animals.core.shared_features.ad.presentation.AdAction
import com.masterplus.animals.core.shared_features.ad.presentation.AdState
import com.masterplus.animals.features.species_list.presentation.SpeciesListPageRoot
import kotlinx.serialization.Serializable

@Serializable
data class SpeciesListRoute(
    val categoryType: CategoryType,
    val kingdomType: KingdomType,
    val categoryItemId: Int?,
    val initPosIndex: Int,
)


fun NavController.navigateToSpeciesList(
    categoryType: CategoryType,
    categoryItemId: Int?,
    kingdomType: KingdomType,
    initPosIndex: Int = 0
){
    navigate(SpeciesListRoute(
        categoryType = categoryType,
        categoryItemId = categoryItemId,
        initPosIndex = initPosIndex,
        kingdomType = kingdomType
    ))
}


fun NavGraphBuilder.speciesList(
    onNavigateBack: () -> Unit,
    onNavigateToSpeciesDetail: (Int, Int?) -> Unit,
    onNavigateToCategorySearch: (CategoryType, ContentType, KingdomType, Int?) -> Unit,
    onNavigateToSavePointSpeciesSettings: () -> Unit,
    adState: AdState,
    onAdAction: (AdAction) -> Unit,
){
    composable<SpeciesListRoute> {
        NavAnimatedVisibilityProvider(
            scope = this
        ){
            SpeciesListPageRoot(
                onNavigateBack = onNavigateBack,
                onNavigateToSpeciesDetail = onNavigateToSpeciesDetail,
                onNavigateToCategorySearch = onNavigateToCategorySearch,
                onNavigateToSavePointSpeciesSettings = onNavigateToSavePointSpeciesSettings,
                onAdAction = onAdAction,
                adState = adState
            )
        }
    }
}