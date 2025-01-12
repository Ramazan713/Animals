package com.masterplus.animals.features.search.presentation.category_search.search_category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.extentions.isAppendItemLoading
import com.masterplus.animals.core.extentions.isEmptyResult
import com.masterplus.animals.core.extentions.isLoading
import com.masterplus.animals.core.extentions.isPrependItemLoading
import com.masterplus.animals.core.extentions.rememberLazyListStatePagingWorkaround
import com.masterplus.animals.core.presentation.components.CategoryItemShipper
import com.masterplus.animals.core.presentation.components.image.ImageWithTitle
import com.masterplus.animals.core.presentation.components.loading.SharedCircularProgress
import com.masterplus.animals.core.presentation.components.loading.SharedLoadingPageContent
import com.masterplus.animals.core.presentation.components.paging.AppendErrorHandlerComponent
import com.masterplus.animals.core.presentation.components.paging.PagingEmptyComponent
import com.masterplus.animals.core.presentation.components.paging.PrependErrorHandlerComponent
import com.masterplus.animals.core.presentation.handlers.categoryNavigateHandler
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.core.presentation.utils.getPreviewLazyPagingData
import com.masterplus.animals.core.presentation.utils.previewPagingLoadStates
import com.masterplus.animals.core.shared_features.ad.presentation.AdAction
import com.masterplus.animals.core.shared_features.ad.presentation.AdState
import com.masterplus.animals.features.search.presentation.category_search.CategorySearchPage
import com.masterplus.animals.features.search.presentation.category_search.CategorySearchState
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchCategoryPageRoot(
    adState: AdState,
    onAdAction: (AdAction) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToSpeciesList: (CategoryType, Int?, KingdomType) -> Unit,
    onNavigateToCategoryListWithDetail: (CategoryType, Int, KingdomType) -> Unit,
    viewModel: SearchCategoryViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val args = viewModel.args
    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()

    CategorySearchPage(
        adState = adState,
        onAdAction = onAdAction,
        state = state,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack
    ){
        SearchResultLazyColumn(
            state = state,
            contentPaddings = it,
            searchResults = searchResults,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onItemClick = { categoryData ->
                val categoryType = if(args.categoryItemId != null) args.categoryType.toChildType() else null
                categoryNavigateHandler(
                    itemId = categoryData.id,
                    categoryType = categoryType ?: args.categoryType,
                    kingdomType = args.kingdomType,
                    onNavigateToSpeciesList = onNavigateToSpeciesList,
                    onNavigateToCategoryListWithDetail = onNavigateToCategoryListWithDetail
                )
            }
        )
    }
}


@Composable
private fun SearchResultLazyColumn(
    state: CategorySearchState,
    contentPaddings: PaddingValues,
    searchResults: LazyPagingItems<CategoryData>,
    onItemClick: (CategoryData) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isSearching by remember(state.searchType, state.isRemoteSearching, searchResults.loadState) {
        derivedStateOf {
            (searchResults.isLoading() && state.searchType.isLocal) || (state.isRemoteSearching && state.searchType.isServer)
        }
    }
    val lazyListState = searchResults.rememberLazyListStatePagingWorkaround()
    SharedLoadingPageContent(
        modifier = modifier,
        isLoading = isSearching,
        overlayLoading = true,
        isEmptyResult = searchResults.isEmptyResult(),
        emptyContent = {
            PagingEmptyComponent(
                pagingItems = searchResults,
                onWatchAd = { }
            )
        },
    ){
        LazyColumn(
            state = lazyListState,
            contentPadding = contentPaddings,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            if(searchResults.isPrependItemLoading()){
                item {
                    SharedCircularProgress(modifier = Modifier.fillMaxWidth())
                }
            }

            PrependErrorHandlerComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                ,
                pagingItems = searchResults,
                onWatchAd = { }
            )
            items(
                count = searchResults.itemCount,
                key = searchResults.itemKey { it.id },
                contentType = searchResults.itemContentType { "MyPagingSearchCategoryPage" }
            ) { index ->
                val item = searchResults[index]
                if (item != null) {
                    ImageWithTitle(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                        model = item,
                        order = index + 1,
                        useTransition = true,
                        onClick = {
                            onItemClick(item)
                        }
                    )
                }else{
                    CategoryItemShipper()
                }
            }
            AppendErrorHandlerComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                ,
                pagingItems = searchResults,
                onWatchAd = { }
            )

            if(searchResults.isAppendItemLoading()){
                item {
                    SharedCircularProgress(modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun SearchCategoryPagePreview() {
    CategorySearchPage(
        state = CategorySearchState(
            query = "a"
        ),
        onAction = {},
        adState = AdState(),
        onAdAction = {},
        onNavigateBack = {},
        searchResultContent = {
            SearchResultLazyColumn(
                contentPaddings = it,
                searchResults = getPreviewLazyPagingData(
                    items = listOf(SampleDatas.categoryData),
                    sourceLoadStates = previewPagingLoadStates()
                ),
                onItemClick = {},
                state = CategorySearchState()
            )
        }
    )
}