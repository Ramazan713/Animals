package com.masterplus.animals.features.species_list.presentation


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.ContentType
import com.masterplus.animals.core.domain.models.SpeciesListDetail
import com.masterplus.animals.core.presentation.components.NavigationBackIcon
import com.masterplus.animals.core.presentation.components.SharedCircularProgress
import com.masterplus.animals.core.presentation.components.SharedLoadingPageContent
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.core.presentation.utils.getPreviewLazyPagingData
import com.masterplus.animals.core.presentation.utils.previewPagingLoadStates
import com.masterplus.animals.core.shared_features.add_species_to_list.presentation.AddSpeciesToListAction
import com.masterplus.animals.core.shared_features.add_species_to_list.presentation.AddSpeciesToListDialogEvent
import com.masterplus.animals.core.shared_features.add_species_to_list.presentation.AddSpeciesToListHandler
import com.masterplus.animals.core.shared_features.add_species_to_list.presentation.AddSpeciesToListState
import com.masterplus.animals.core.shared_features.add_species_to_list.presentation.AddSpeciesToListViewModel
import com.masterplus.animals.core.shared_features.savepoint.data.mapper.toSavePointDestinationTypeId
import com.masterplus.animals.core.shared_features.savepoint.domain.models.EditSavePointLoadParam
import com.masterplus.animals.core.shared_features.savepoint.presentation.edit_savepoint.EditSavePointDialog
import com.masterplus.animals.features.species_list.domain.enums.SpeciesListItemMenu
import com.masterplus.animals.features.species_list.presentation.components.SpeciesCard
import com.masterplus.animals.features.species_list.presentation.navigation.SpeciesListRoute
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun SpeciesListPageRoot(
    viewModel: SpeciesListViewModel = koinViewModel(),
    addSpeciesToListViewModel: AddSpeciesToListViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToSpeciesDetail: (Int) -> Unit,
    onNavigateToCategorySearch: (CategoryType, ContentType, Int?) -> Unit
) {
    val args = viewModel.args
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pagingItems = viewModel.pagingItems.collectAsLazyPagingItems()

    val addSpeciesState by addSpeciesToListViewModel.state.collectAsStateWithLifecycle()
    SpeciesListPage(
        state = state,
        onAction = viewModel::onAction,
        addSpeciesState = addSpeciesState,
        onAddSpeciesAction = addSpeciesToListViewModel::onAction,
        args = args,
        onNavigateBack = onNavigateBack,
        onNavigateToSpeciesDetail = onNavigateToSpeciesDetail,
        pagingItems = pagingItems,
        onNavigateToCategorySearch = {
            onNavigateToCategorySearch(args.categoryType, ContentType.Category, args.realItemId)
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun SpeciesListPage(
    pagingItems: LazyPagingItems<SpeciesListDetail>,
    state: SpeciesListState,
    onAction: (SpeciesListAction) -> Unit,
    addSpeciesState: AddSpeciesToListState,
    onAddSpeciesAction: (AddSpeciesToListAction) -> Unit,
    args: SpeciesListRoute,
    onNavigateBack: () -> Unit,
    onNavigateToSpeciesDetail: (Int) -> Unit,
    onNavigateToCategorySearch: () -> Unit
) {
    val lazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = args.initPosIndex
    )
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = state.title.asString())
                },
                navigationIcon = {
                    NavigationBackIcon(onNavigateBack = onNavigateBack)
                },
                actions = {
                    IconButton(onClick = onNavigateToCategorySearch) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    }
                }
            )
        }
    ) { paddings ->
        SharedLoadingPageContent(
            modifier = Modifier
                .padding(paddings)
                .fillMaxSize(),
            isLoading = pagingItems.loadState.refresh is LoadState.Loading,
            isEmptyResult = pagingItems.itemCount == 0,
            overlayLoading = true
        ) {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .matchParentSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp)
            ) {
                items(
                    count = pagingItems.itemCount,
                    key = { pagingItems[it]?.id ?: it }
                ){ index ->
                    val item = pagingItems[index]
                    if(item != null){
                        SpeciesCard(
                            species = item,
                            orderNum = index + 1,
                            isFavorited = item.isFavorited,
                            onClick = {
                                onNavigateToSpeciesDetail(item.id ?: 0)
                            },
                            onFavoriteClick = {
                                onAddSpeciesAction(AddSpeciesToListAction.AddToFavorite(item.id ?: 0))
                            },
                            onUnFavoriteClick = {
                                onAddSpeciesAction(AddSpeciesToListAction.AddOrAskFavorite(item.id ?: 0))
                            },
                            onMenuButtonClick = {
                                onAddSpeciesAction(AddSpeciesToListAction.ShowDialog(AddSpeciesToListDialogEvent.ShowItemBottomMenu(item,index)))
                            },
                        )
                    }
                }
                if(pagingItems.loadState.append is LoadState.Loading){
                    item {
                        SharedCircularProgress(modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }

    AddSpeciesToListHandler(
        state = addSpeciesState,
        onAction = onAddSpeciesAction,
        listIdControl = state.listIdControl,
        bottomMenuItems = SpeciesListItemMenu.entries,
        onBottomMenuItemClick = { menuItem, item, pos ->
            when(menuItem){
                SpeciesListItemMenu.Savepoint -> {
                    onAction(SpeciesListAction.ShowDialog(SpeciesListDialogEvent.ShowEditSavePoint(item, pos)))
                }
            }
        }
    )

    state.dialogEvent?.let { dialogEvent ->
        val close = remember {{
            onAction(SpeciesListAction.ShowDialog(null))
        }}
        when(dialogEvent){
            is SpeciesListDialogEvent.ShowEditSavePoint -> {
                EditSavePointDialog(
                    loadParam = EditSavePointLoadParam(
                        destinationTypeId = args.categoryType.toSavePointDestinationTypeId(args.realItemId),
                        destinationId = args.realItemId
                    ),
                    posIndex = dialogEvent.posIndex,
                    onClosed = close,
                    onNavigateLoad = {
                        scope.launch {
                            lazyListState.animateScrollToItem(it.itemPosIndex)
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SpeciesListPagePreview() {
    SpeciesListPage(
        state = SpeciesListState(),
        addSpeciesState = AddSpeciesToListState(),
        onAction = {},
        onAddSpeciesAction = {},
        onNavigateBack = {},
        args = SpeciesListRoute(
            categoryId = 1,
            itemId = 1,
            initPosIndex = 0
        ),
        onNavigateToSpeciesDetail = {},
        pagingItems = getPreviewLazyPagingData(
            items = listOf(SampleDatas.generateSpeciesDetail(id = 1), SampleDatas.generateSpeciesDetail(id = 2), SampleDatas.generateSpeciesDetail(id = 3)),
            sourceLoadStates = previewPagingLoadStates(refresh = LoadState.Loading, append = LoadState.Loading),
        ),
        onNavigateToCategorySearch = {}
    )
}