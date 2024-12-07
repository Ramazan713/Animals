package com.masterplus.animals.features.search.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.ContentType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.features.search.presentation.category_search.search_category.SearchCategoryPageRoot
import kotlinx.serialization.Serializable

@Serializable
data class SearchCategoryRoute(
    val contentTypeId: Int,
    val categoryId: Int,
    val kingdomId: Int,
    val itemId: Int
){
    val contentType get() =  ContentType.from(contentTypeId)
    val categoryType get() =  CategoryType.fromCatId(categoryId)
    val realItemId get() = if(itemId == 0) null else itemId
    val kingdomType get() = KingdomType.fromKingdomId(kingdomId)
}

fun NavController.navigateToSearchCategory(categoryType: CategoryType, contentType: ContentType, itemId: Int?, kingdomType: KingdomType){
    navigate(SearchCategoryRoute(
        categoryId = categoryType.catId,
        contentTypeId = contentType.contentTypeId,
        itemId = itemId ?: 0,
        kingdomId = kingdomType.kingdomId
    ))
}

fun NavGraphBuilder.searchCategory(
    onNavigateBack: () -> Unit,
    onNavigateToSpeciesList: (CategoryType, Int?, KingdomType) -> Unit,
    onNavigateToCategoryListWithDetail: (CategoryType, Int, KingdomType) -> Unit,
){
    composable<SearchCategoryRoute> {
        SearchCategoryPageRoot(
            onNavigateBack = onNavigateBack,
            onNavigateToSpeciesList = onNavigateToSpeciesList,
            onNavigateToCategoryListWithDetail = onNavigateToCategoryListWithDetail
        )
    }
}