package com.masterplus.animals.features.category_list.presentation.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.features.category_list.presentation.CategoryListPage
import com.masterplus.animals.features.category_list.presentation.CategoryListViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
data class CategoryListRoute(
    val categoryId: Int
){
    val categoryType get() = CategoryType.fromCatId(categoryId)
}

fun NavController.navigateToCategoryList(categoryType: CategoryType){
    navigate(CategoryListRoute(categoryType.catId))
}

fun NavGraphBuilder.categoryList(
    onNavigateBack: () -> Unit,
    onNavigateToBioList: (CategoryType, Int?) -> Unit,
    onNavigateToCategoryListWithDetail: (CategoryType, Int) -> Unit,
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
                if(args.categoryType == CategoryType.Family){
                    onNavigateToBioList(args.categoryType, item.id)
                }else{
                    onNavigateToCategoryListWithDetail(args.categoryType, item.id ?: 0)
                }
            },
            onAllItemClick = {
                onNavigateToBioList(args.categoryType, null)
            }
        )
    }
}