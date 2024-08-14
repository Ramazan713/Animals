package com.masterplus.animals.features.search.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.ContentType
import com.masterplus.animals.features.search.presentation.category_search.CategorySpeciesSearchPageRoot
import kotlinx.serialization.Serializable

@Serializable
data class CategorySpeciesSearchRoute(
    val contentTypeId: Int,
    val categoryId: Int,
    val itemId: Int
){
    val contentType get() =  ContentType.from(contentTypeId)
    val categoryType get() =  CategoryType.fromCatId(categoryId)
}

fun NavController.navigateToCategorySpeciesSearch(categoryType: CategoryType, contentType: ContentType, itemId: Int){
    navigate(CategorySpeciesSearchRoute(
        categoryId = categoryType.catId,
        contentTypeId = contentType.contentTypeId,
        itemId = itemId
    ))
}

fun NavGraphBuilder.categorySpeciesSearch(
    onNavigateBack: () -> Unit
){
    composable<CategorySpeciesSearchRoute> {
        CategorySpeciesSearchPageRoot(
            onNavigateBack = onNavigateBack
        )
    }
}