package com.masterplus.animals.features.search.presentation.category_search.search_species

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.masterplus.animals.core.domain.models.SpeciesListDetail
import com.masterplus.animals.core.extentions.isAppendItemLoading
import com.masterplus.animals.core.extentions.isEmptyResult
import com.masterplus.animals.core.extentions.isLoading
import com.masterplus.animals.core.extentions.isPrependItemLoading
import com.masterplus.animals.core.presentation.components.SpeciesCard
import com.masterplus.animals.core.presentation.components.SpeciesCardShimmer
import com.masterplus.animals.core.presentation.components.loading.SharedCircularProgress
import com.masterplus.animals.core.presentation.components.loading.SharedLoadingPageContent
import com.masterplus.animals.core.presentation.components.paging.AppendErrorHandlerComponent
import com.masterplus.animals.core.presentation.components.paging.PagingEmptyComponent
import com.masterplus.animals.core.presentation.components.paging.PrependErrorHandlerComponent
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.core.presentation.utils.getPreviewLazyPagingData
import com.masterplus.animals.core.presentation.utils.previewPagingLoadStates
import com.masterplus.animals.core.shared_features.ad.presentation.AdAction
import com.masterplus.animals.core.shared_features.ad.presentation.AdUiResult
import com.masterplus.animals.core.shared_features.add_species_to_list.presentation.AddSpeciesToListAction
import com.masterplus.animals.core.shared_features.add_species_to_list.presentation.AddSpeciesToListDialogEvent
import com.masterplus.animals.core.shared_features.add_species_to_list.presentation.AddSpeciesToListHandler
import com.masterplus.animals.core.shared_features.add_species_to_list.presentation.AddSpeciesToListViewModel
import com.masterplus.animals.features.search.presentation.category_search.CategorySearchPage
import com.masterplus.animals.features.search.presentation.category_search.CategorySearchState
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchSpeciesPageRoot(
    adUiResult: AdUiResult?,
    onAdAction: (AdAction) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToSpeciesDetail: (Int) -> Unit,
    viewModel: SearchSpeciesViewModel = koinViewModel(),
    addSpeciesToListViewModel: AddSpeciesToListViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val searchResults = viewModel.searchResults.collectAsLazyPagingItems()
    val args = viewModel.args

    val addSpeciesState by addSpeciesToListViewModel.state.collectAsStateWithLifecycle()

    AddSpeciesToListHandler(
        state = addSpeciesState,
        onAction = addSpeciesToListViewModel::onAction,
        listIdControl = args.categoryType.toListIdControlOrNull(args.categoryItemId)
    )

    CategorySearchPage(
        adUiResult = adUiResult,
        onAdAction = onAdAction,
        state = state,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack
    ){
        SearchResultLazyColumn(
            state = state,
            contentPaddings = it,
            searchResults = searchResults,
            onAddSpeciesAction = addSpeciesToListViewModel::onAction,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onItemClick = {
                onNavigateToSpeciesDetail(it.id)
            }
        )
    }
}



@Composable
private fun SearchResultLazyColumn(
    state: CategorySearchState,
    contentPaddings: PaddingValues,
    searchResults: LazyPagingItems<SpeciesListDetail>,
    onAddSpeciesAction: (AddSpeciesToListAction) -> Unit,
    onItemClick: (SpeciesListDetail) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isSearching by remember(state.searchType, state.isRemoteSearching, searchResults.loadState) {
        derivedStateOf {
            (searchResults.isLoading() && state.searchType.isLocal) || (state.isRemoteSearching && state.searchType.isServer)
        }
    }
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
            contentPadding = contentPaddings,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            if(searchResults.isPrependItemLoading()){
                item {
                    SharedCircularProgress(modifier = Modifier.fillMaxWidth())
                }
            }

            PrependErrorHandlerComponent(
                modifier = Modifier.fillMaxWidth(),
                pagingItems = searchResults,
                onWatchAd = { }
            )

            items(
                count = searchResults.itemCount,
                key = searchResults.itemKey { it.id },
                contentType = searchResults.itemContentType { "MyPagingSearchList" }
            ) { index ->
                val item = searchResults[index]
                if (item != null) {
                    SpeciesCard(
                        species = item,
                        orderNum = "${index + 1}",
                        isFavorited = item.isFavorited,
                        onClick = {
                            onItemClick(item)
                        },
                        onFavoriteClick = {
                            onAddSpeciesAction(AddSpeciesToListAction.AddToFavorite(item.id ?: 0))
                        },
                        onUnFavoriteClick = {
                            onAddSpeciesAction(AddSpeciesToListAction.AddOrAskFavorite(item.id ?: 0))
                        },
                        onMenuButtonClick = {
                            onAddSpeciesAction(AddSpeciesToListAction.ShowDialog(
                                AddSpeciesToListDialogEvent.ShowItemBottomMenu(
                                    speciesId = item.id,
                                    speciesName = item.name,
                                    posIndex = index,
                                    orderKey = item.orderKey
                                )))
                        },
                    )
                }else{
                    SpeciesCardShimmer()
                }
            }
            AppendErrorHandlerComponent(
                modifier = Modifier.fillMaxWidth(),
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
        onNavigateBack = {},
        adUiResult = null,
        onAdAction = {},
        searchResultContent = {
            SearchResultLazyColumn(
                contentPaddings = it,
                searchResults = getPreviewLazyPagingData(
                    items = listOf(SampleDatas.generateSpeciesDetail()),
                    sourceLoadStates = previewPagingLoadStates()
                ),
                onAddSpeciesAction = {},
                onItemClick = {},
                state = CategorySearchState(),
            )
        }
    )
}