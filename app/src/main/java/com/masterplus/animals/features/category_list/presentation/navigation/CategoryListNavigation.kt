package com.masterplus.animals.features.category_list.presentation.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.ContentType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.presentation.handlers.categoryNavigateHandler
import com.masterplus.animals.features.category_list.presentation.CategoryListPage
import com.masterplus.animals.features.category_list.presentation.CategoryListViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
data class CategoryListRoute(
    val categoryId: Int,
    val kingdomId: Int
){
    val categoryType get() = CategoryType.fromCatId(categoryId)
    val kingdomType get() = KingdomType.fromKingdomId(kingdomId)
}

fun NavController.navigateToCategoryList(categoryType: CategoryType, kingdomType: KingdomType){
    navigate(CategoryListRoute(
        categoryId = categoryType.catId,
        kingdomId = kingdomType.kingdomId
    ))
}

fun NavGraphBuilder.categoryList(
    onNavigateBack: () -> Unit,
    onNavigateToSpeciesList: (CategoryType, Int?) -> Unit,
    onNavigateToCategoryListWithDetail: (CategoryType, Int, KingdomType) -> Unit,
    onNavigateToCategorySearch: (CategoryType, ContentType, KingdomType) -> Unit
){
    composable<CategoryListRoute> {
        val viewModel: CategoryListViewModel = koinViewModel()
        val args = viewModel.args
        val state by viewModel.state.collectAsStateWithLifecycle()
        val items = viewModel.pagingItems.collectAsLazyPagingItems()
        CategoryListPage(
            state = state,
            onAction = viewModel::onAction,
            pagingItems = items,
            onNavigateBack = onNavigateBack,
            onItemClick = { item ->
                categoryNavigateHandler(
                    itemId = item.id,
                    categoryType = args.categoryType,
                    kingdomType = args.kingdomType,
                    onNavigateToSpeciesList = onNavigateToSpeciesList,
                    onNavigateToCategoryListWithDetail = onNavigateToCategoryListWithDetail
                )
            },
            onAllItemClick = {
                onNavigateToSpeciesList(args.categoryType, null)
            },
            onNavigateToCategorySearch = {
                onNavigateToCategorySearch(args.categoryType, ContentType.Category, args.kingdomType)
            }
        )
    }
}