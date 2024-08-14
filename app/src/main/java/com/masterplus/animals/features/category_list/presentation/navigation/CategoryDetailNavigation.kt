package com.masterplus.animals.features.category_list.presentation.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.ContentType
import com.masterplus.animals.features.category_list.presentation.CategoryListPage
import com.masterplus.animals.features.category_list.presentation.CategoryListWithDetailViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
data class CategoryListWithDetailRoute(
    val categoryId: Int,
    val itemId: Int
){
    val categoryType get() = CategoryType.fromCatId(categoryId)
}

fun NavController.navigateToCategoryListWithDetail(categoryType: CategoryType, itemId: Int){
    navigate(CategoryListWithDetailRoute(categoryType.catId, itemId))
}

fun NavGraphBuilder.categoryListWithDetail(
    onNavigateBack: () -> Unit,
    onNavigateToSpeciesList: (CategoryType, Int?) -> Unit,
    onNavigateToCategoryListWithDetail: (CategoryType, Int) -> Unit,
    onNavigateToCategorySearch: (CategoryType, ContentType, Int) -> Unit
){
    composable<CategoryListWithDetailRoute> {
        val viewModel: CategoryListWithDetailViewModel = koinViewModel()
        val args = viewModel.args
        val state by viewModel.state.collectAsStateWithLifecycle()
        val items = viewModel.pagingItems.collectAsLazyPagingItems()
        CategoryListPage(
            state = state,
            onAction = viewModel::onAction,
            pagingItems = items,
            onNavigateBack = onNavigateBack,
            onItemClick = { item ->
                val detailCategoryType = viewModel.getDetailCategoryType()
                if(detailCategoryType == CategoryType.Family){
                    onNavigateToSpeciesList(viewModel.getDetailCategoryType(), item.id ?: 0)
                }else{
                    onNavigateToCategoryListWithDetail(viewModel.getDetailCategoryType(), item.id ?: 0)
                }
            },
            onAllItemClick = {
                onNavigateToSpeciesList(args.categoryType, args.itemId)
            },
            onNavigateToCategorySearch = {
                onNavigateToCategorySearch(args.categoryType, ContentType.Category, args.itemId)
            }
        )
    }
}