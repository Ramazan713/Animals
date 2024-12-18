package com.masterplus.animals.features.search.presentation.category_search.search_category

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.presentation.components.image.ImageWithTitle
import com.masterplus.animals.core.presentation.components.loading.SharedCircularProgress
import com.masterplus.animals.core.presentation.components.loading.SharedLoadingPageContent
import com.masterplus.animals.core.presentation.handlers.categoryNavigateHandler
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.core.presentation.utils.getPreviewLazyPagingData
import com.masterplus.animals.core.presentation.utils.previewPagingLoadStates
import com.masterplus.animals.features.search.presentation.category_search.CategorySearchPage
import com.masterplus.animals.features.search.presentation.category_search.CategorySearchState
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchCategoryPageRoot(
    onNavigateBack: () -> Unit,
    onNavigateToSpeciesList: (CategoryType, Int?, KingdomType) -> Unit,
    onNavigateToCategoryListWithDetail: (CategoryType, Int, KingdomType) -> Unit,
    viewModel: SearchCategoryViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val args = viewModel.args
    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()

    CategorySearchPage(
        state = state,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack
    ){
        SearchResultLazyColumn(
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
    contentPaddings: PaddingValues,
    searchResults: LazyPagingItems<CategoryData>,
    onItemClick: (CategoryData) -> Unit,
    modifier: Modifier = Modifier,
) {
    SharedLoadingPageContent(
        modifier = modifier,
        isLoading = searchResults.loadState.refresh is LoadState.Loading,
        overlayLoading = true,
        isEmptyResult = searchResults.itemCount == 0
    ){
        LazyColumn(
            contentPadding = contentPaddings,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(
                count = searchResults.itemCount,
                key = { searchResults[it]?.id ?: it },
            ) { index ->
                val item = searchResults[index]
                if (item != null) {
                    ImageWithTitle(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                        model = item,
                        order = index + 1,
                        onClick = {
                            onItemClick(item)
                        }
                    )
                }
            }
            if (searchResults.loadState.append is LoadState.Loading) {
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
        onNavigateBack = {},
        searchResultContent = {
            SearchResultLazyColumn(
                contentPaddings = it,
                searchResults = getPreviewLazyPagingData(
                    items = listOf(SampleDatas.categoryData),
                    sourceLoadStates = previewPagingLoadStates()
                ),
                onItemClick = {}
            )
        }
    )
}