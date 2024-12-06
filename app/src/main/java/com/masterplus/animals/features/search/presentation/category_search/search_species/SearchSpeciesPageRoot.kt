package com.masterplus.animals.features.search.presentation.category_search.search_species

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
import com.masterplus.animals.core.domain.models.SpeciesListDetail
import com.masterplus.animals.core.presentation.components.SharedCircularProgress
import com.masterplus.animals.core.presentation.components.SharedLoadingPageContent
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.core.presentation.utils.getPreviewLazyPagingData
import com.masterplus.animals.core.presentation.utils.previewPagingLoadStates
import com.masterplus.animals.core.shared_features.add_species_to_list.presentation.AddSpeciesToListAction
import com.masterplus.animals.core.shared_features.add_species_to_list.presentation.AddSpeciesToListDialogEvent
import com.masterplus.animals.core.shared_features.add_species_to_list.presentation.AddSpeciesToListHandler
import com.masterplus.animals.core.shared_features.add_species_to_list.presentation.AddSpeciesToListViewModel
import com.masterplus.animals.features.search.presentation.category_search.CategorySearchPage
import com.masterplus.animals.features.search.presentation.category_search.CategorySearchState
import com.masterplus.animals.features.species_list.presentation.components.SpeciesCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchSpeciesPageRoot(
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
        listIdControl = if(args.categoryType == CategoryType.List) args.realItemId else null
    )

    CategorySearchPage(
        state = state,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack
    ){
        SearchResultLazyColumn(
            contentPaddings = it,
            searchResults = searchResults,
            onAddSpeciesAction = addSpeciesToListViewModel::onAction,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onItemClick = {
                onNavigateToSpeciesDetail(it.id ?: 0)
            }
        )
    }
}



@Composable
private fun SearchResultLazyColumn(
    contentPaddings: PaddingValues,
    searchResults: LazyPagingItems<SpeciesListDetail>,
    onAddSpeciesAction: (AddSpeciesToListAction) -> Unit,
    onItemClick: (SpeciesListDetail) -> Unit,
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
                                AddSpeciesToListDialogEvent.ShowItemBottomMenu(item,index)))
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
                    items = listOf(SampleDatas.generateSpeciesDetail()),
                    sourceLoadStates = previewPagingLoadStates()
                ),
                onAddSpeciesAction = {},
                onItemClick = {}
            )
        }
    )
}