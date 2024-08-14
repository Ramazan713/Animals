package com.masterplus.animals.features.search.presentation.category_search


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.masterplus.animals.R
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.domain.models.SpeciesDetail
import com.masterplus.animals.core.extentions.clearFocusOnTap
import com.masterplus.animals.core.presentation.components.ImageWithTitle
import com.masterplus.animals.core.presentation.components.SharedCircularProgress
import com.masterplus.animals.core.presentation.components.SharedLoadingPageContent
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.core.presentation.utils.getPreviewLazyPagingData
import com.masterplus.animals.core.presentation.utils.previewPagingLoadStates
import com.masterplus.animals.features.search.presentation.components.SearchField
import com.masterplus.animals.features.species_list.presentation.components.SpeciesCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun CategorySearchPageRoot(
    onNavigateBack: () -> Unit,
    viewModel: CategorySearchViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
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
                .weight(1f)
        )
    }
}


@Composable
fun CategorySpeciesSearchPageRoot(
    onNavigateBack: () -> Unit,
    viewModel: CategorySpeciesSearchViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()

    CategorySearchPage(
        state = state,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack
    ){
        SearchResult2LazyColumn(
            contentPaddings = it,
            searchResults = searchResults,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        )
    }
}



@Composable
fun CategorySearchPage(
    state: CategorySearchState,
    onAction: (CategorySearchAction) -> Unit,
    onNavigateBack: () -> Unit,
    searchResultContent: @Composable (ColumnScope.(PaddingValues) -> Unit)
) {

    val contentPadding = PaddingValues(
        bottom = 16.dp,
        top = 16.dp
    )

    Scaffold { paddings ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddings)
                .padding(horizontal = 12.dp)
                .semantics { isTraversalGroup = true }
                .clearFocusOnTap()
        ) {
            GetSearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                state = state,
                onAction = onAction,
                onNavigateBack = onNavigateBack
            )
            AnimatedVisibility(
                visible = state.query.isBlank()
            ) {
                HistoryLazyColumn(
                    contentPaddings = contentPadding,
                    modifier = Modifier.weight(1f)
                )
            }

            AnimatedVisibility(
                visible = state.query.isNotBlank()
            ) {
                searchResultContent(contentPadding)
            }
        }
    }
}

@Composable
private fun GetSearchBar(
    state: CategorySearchState,
    onAction: (CategorySearchAction) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val placeholder = state.titleForPlaceHolder?.asString()?.let { titleForPlaceHolder ->
        stringResource(id = R.string.n_search_in, titleForPlaceHolder)
    } ?: "Ara"
    SearchField(
        modifier = modifier,
        query = state.query,
        onValueChange = { onAction(CategorySearchAction.SearchQuery(it)) },
        onBackPressed = onNavigateBack,
        onClear = {
            onAction(CategorySearchAction.SearchQuery(""))
        },
        placeholder = placeholder
    )
}

@Composable
private fun HistoryLazyColumn(
    modifier: Modifier = Modifier,
    contentPaddings: PaddingValues
) {
    LazyColumn(
        contentPadding = contentPaddings,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        items(
            count = 3,
        ) { index ->
            Text(
                text = "Search item $index"
            )
        }
    }
}


@Composable
private fun SearchResultLazyColumn(
    contentPaddings: PaddingValues,
    searchResults: LazyPagingItems<CategoryData>,
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
                .matchParentSize()
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


@Composable
private fun SearchResult2LazyColumn(
    contentPaddings: PaddingValues,
    searchResults: LazyPagingItems<SpeciesDetail>,
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
                    SpeciesCard(
                        species = item,
                        orderNum = index + 1,
                        isFavorited = item.isFavorited,
                        onClick = {

                        },
                        onFavoriteClick = {

                        },
                        onUnFavoriteClick = {

                        },
                        onMenuButtonClick = {

                        },
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
private fun CategorySearchPagePreview() {
    CategorySearchPage(
        state = CategorySearchState(
            query = "a"
        ),
        onAction = {},
        onNavigateBack = {},
        searchResultContent = {
            SearchResultLazyColumn(contentPaddings = it, searchResults = getPreviewLazyPagingData(items = listOf(
//                SampleDatas.categoryData
            ), previewPagingLoadStates()))
        }
    )
}