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
import com.masterplus.animals.core.presentation.transition.NavAnimatedVisibilityProvider
import com.masterplus.animals.core.shared_features.ad.presentation.AdAction
import com.masterplus.animals.core.shared_features.ad.presentation.AdState
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import com.masterplus.animals.core.shared_features.savepoint.presentation.auto_savepoint.AutoSavePointViewModel
import com.masterplus.animals.features.category_list.presentation.CategoryListPage
import com.masterplus.animals.features.category_list.presentation.CategoryListWithDetailViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
data class CategoryListWithDetailRoute(
    val categoryType: CategoryType,
    val kingdomType: KingdomType,
    val categoryItemId: Int
)

fun NavController.navigateToCategoryListWithDetail(
    categoryType: CategoryType,
    categoryItemId: Int,
    kingdomType: KingdomType
){
    navigate(CategoryListWithDetailRoute(
        categoryType = categoryType,
        kingdomType = kingdomType,
        categoryItemId = categoryItemId,
    ))
}

fun NavGraphBuilder.categoryListWithDetail(
    onNavigateBack: () -> Unit,
    onNavigateToSpeciesList: (CategoryType, Int?, KingdomType) -> Unit,
    onNavigateToCategoryListWithDetail: (CategoryType, Int, KingdomType) -> Unit,
    onNavigateToCategorySearch: (CategoryType, ContentType, Int, KingdomType) -> Unit,
    onNavigateToSavePointCategorySettings: () -> Unit,
    adState: AdState,
    onAdAction: (AdAction) -> Unit,
){
    composable<CategoryListWithDetailRoute> {
        val viewModel: CategoryListWithDetailViewModel = koinViewModel()
        val autoSavePointViewModel: AutoSavePointViewModel = koinViewModel()

        val args = viewModel.args
        val state by viewModel.state.collectAsStateWithLifecycle()
        val items = viewModel.pagingItems.collectAsLazyPagingItems()

        val autoSavePointState by autoSavePointViewModel.state.collectAsStateWithLifecycle()

        NavAnimatedVisibilityProvider(
            scope = this
        ){
            CategoryListPage(
                state = state,
                onAction = viewModel::onAction,
                pagingItems = items,
                onNavigateBack = onNavigateBack,
                onItemClick = { item ->
                    categoryNavigateHandler(
                        itemId = item.id,
                        categoryType = args.categoryType.toChildType() ?: args.categoryType,
                        kingdomType = args.kingdomType,
                        onNavigateToSpeciesList = onNavigateToSpeciesList,
                        onNavigateToCategoryListWithDetail = onNavigateToCategoryListWithDetail
                    )
                },
                onAllItemClick = {
                    onNavigateToSpeciesList(args.categoryType, args.categoryItemId, args.kingdomType)
                },
                onNavigateToCategorySearch = {
                    onNavigateToCategorySearch(args.categoryType, ContentType.Category, args.categoryItemId, args.kingdomType)
                },
                autoSavePointState = autoSavePointState,
                onAutoSavePointAction = autoSavePointViewModel::onAction,
                onDestination = {
                    SavePointDestination.fromCategoryType(
                        categoryType = args.categoryType,
                        kingdomType = args.kingdomType,
                        destinationId = args.categoryItemId,
                        returnAll = false
                    )
                },
                onNavigateToSavePointCategorySettings = onNavigateToSavePointCategorySettings,
                onAdAction = onAdAction,
                adState = adState
            )
        }
    }
}