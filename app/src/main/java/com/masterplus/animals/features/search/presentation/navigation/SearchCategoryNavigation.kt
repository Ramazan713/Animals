package com.masterplus.animals.features.search.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.ContentType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.shared_features.ad.presentation.AdAction
import com.masterplus.animals.core.shared_features.ad.presentation.AdState
import com.masterplus.animals.core.shared_features.ad.presentation.AdUiResult
import com.masterplus.animals.features.search.presentation.category_search.search_category.SearchCategoryPageRoot
import kotlinx.serialization.Serializable

@Serializable
data class SearchCategoryRoute(
    val contentType: ContentType,
    val categoryType: CategoryType,
    val kingdomType: KingdomType,
    val categoryItemId: Int?
)

fun NavController.navigateToSearchCategory(
    categoryType: CategoryType,
    contentType: ContentType,
    categoryItemId: Int?,
    kingdomType: KingdomType
){
    navigate(SearchCategoryRoute(
        categoryType = categoryType,
        contentType = contentType,
        categoryItemId = categoryItemId,
        kingdomType = kingdomType
    ))
}

fun NavGraphBuilder.searchCategory(
    adState: AdState,
    onAdAction: (AdAction) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToSpeciesList: (CategoryType, Int?, KingdomType) -> Unit,
    onNavigateToCategoryListWithDetail: (CategoryType, Int, KingdomType) -> Unit,
){
    composable<SearchCategoryRoute> {
        SearchCategoryPageRoot(
            adState = adState,
            onAdAction = onAdAction,
            onNavigateBack = onNavigateBack,
            onNavigateToSpeciesList = onNavigateToSpeciesList,
            onNavigateToCategoryListWithDetail = onNavigateToCategoryListWithDetail
        )
    }
}