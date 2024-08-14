package com.masterplus.animals.features.search.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.ContentType
import com.masterplus.animals.features.search.presentation.category_search.search_species.SearchSpeciesPageRoot
import kotlinx.serialization.Serializable

@Serializable
data class SearchSpeciesRoute(
    val contentTypeId: Int,
    val categoryId: Int,
    val itemId: Int
){
    val contentType get() =  ContentType.from(contentTypeId)
    val categoryType get() =  CategoryType.fromCatId(categoryId)
    val realItemId get() = if(itemId == 0) null else itemId
}

fun NavController.navigateToSearchSpecies(categoryType: CategoryType, contentType: ContentType, itemId: Int?){
    navigate(SearchSpeciesRoute(
        categoryId = categoryType.catId,
        contentTypeId = contentType.contentTypeId,
        itemId = itemId ?: 0
    ))
}

fun NavGraphBuilder.searchSpecies(
    onNavigateBack: () -> Unit,
    onNavigateToSpeciesDetail: (Int) -> Unit,
){
    composable<SearchSpeciesRoute> {
        SearchSpeciesPageRoot(
            onNavigateBack = onNavigateBack,
            onNavigateToSpeciesDetail = onNavigateToSpeciesDetail
        )
    }
}