package com.masterplus.animals.features.search.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.ContentType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.presentation.transition.NavAnimatedVisibilityProvider
import com.masterplus.animals.core.shared_features.ad.presentation.AdAction
import com.masterplus.animals.core.shared_features.ad.presentation.AdState
import com.masterplus.animals.core.shared_features.ad.presentation.AdUiResult
import com.masterplus.animals.features.search.presentation.category_search.search_species.SearchSpeciesPageRoot
import kotlinx.serialization.Serializable

@Serializable
data class SearchSpeciesRoute(
    val contentType: ContentType,
    val categoryType: CategoryType,
    val kingdomType: KingdomType,
    val categoryItemId: Int?
)

fun NavController.navigateToSearchSpecies(
    categoryType: CategoryType,
    contentType: ContentType,
    kingdomType: KingdomType,
    categoryItemId: Int?
){
    navigate(SearchSpeciesRoute(
        categoryType = categoryType,
        contentType = contentType,
        categoryItemId = categoryItemId,
        kingdomType = kingdomType
    ))
}

fun NavGraphBuilder.searchSpecies(
    adState: AdState,
    onAdAction: (AdAction) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToSpeciesDetail: (Int) -> Unit,
){
    composable<SearchSpeciesRoute> {
        NavAnimatedVisibilityProvider(
            scope = this
        ){
            SearchSpeciesPageRoot(
                adState = adState,
                onAdAction = onAdAction,
                onNavigateBack = onNavigateBack,
                onNavigateToSpeciesDetail = onNavigateToSpeciesDetail
            )
        }
    }
}