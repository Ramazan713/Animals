package com.masterplus.animals.features.bio_list.presentation


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.masterplus.animals.R
import com.masterplus.animals.core.domain.models.SpeciesDetail
import com.masterplus.animals.core.presentation.components.NavigationBackIcon
import com.masterplus.animals.core.presentation.components.SharedCircularProgress
import com.masterplus.animals.core.presentation.components.SharedLoadingPageContent
import com.masterplus.animals.core.presentation.dialogs.ShowQuestionDialog
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.core.presentation.utils.getPreviewLazyPagingData
import com.masterplus.animals.core.presentation.utils.previewPagingLoadStates
import com.masterplus.animals.core.shared_features.list.presentation.select_list_with_menu.ShowBottomMenuWithSelectList
import com.masterplus.animals.core.shared_features.savepoint.data.mapper.toSavePointDestinationTypeId
import com.masterplus.animals.core.shared_features.savepoint.domain.models.EditSavePointLoadParam
import com.masterplus.animals.core.shared_features.savepoint.presentation.edit_savepoint.EditSavePointDialog
import com.masterplus.animals.features.bio_list.domain.enums.BioListItemMenu
import com.masterplus.animals.features.bio_list.presentation.components.BioCard
import com.masterplus.animals.features.bio_list.presentation.navigation.BioListRoute
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun BioListPageRoot(
    viewModel: BioListViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToBioDetail: (Int) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pagingItems = viewModel.pagingItems.collectAsLazyPagingItems()

    BioListPage(
        state = state,
        onAction = viewModel::onAction,
        args = viewModel.args,
        onNavigateBack = onNavigateBack,
        onNavigateToBioDetail = onNavigateToBioDetail,
        pagingItems = pagingItems
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun BioListPage(
    pagingItems: LazyPagingItems<SpeciesDetail>,
    state: BioListState,
    args: BioListRoute,
    onAction: (BioListAction) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToBioDetail: (Int) -> Unit
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
                        BioCard(
                            species = item,
                            orderNum = index + 1,
                            isFavorited = item.isFavorited,
                            onClick = {
                                onNavigateToBioDetail(item.id ?: 0)
                            },
                            onFavoriteClick = {
                                onAction(BioListAction.AddToFavorite(item.id ?: 0))
                            },
                            onUnFavoriteClick = {
                                onAction(BioListAction.AddOrAskFavorite(item.id ?: 0))
                            },
                            onMenuButtonClick = {
                                onAction(BioListAction.ShowDialog(BioListDialogEvent.ShowItemBottomMenu(item,index)))
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


    state.dialogEvent?.let { dialogEvent ->
        val close = remember {{
            onAction(BioListAction.ShowDialog(null))
        }}
        when(dialogEvent){
            is BioListDialogEvent.ShowItemBottomMenu -> {
                ShowBottomMenuWithSelectList(
                    items = BioListItemMenu.entries,
                    title = stringResource(id = R.string.n_for_number_word,dialogEvent.posIndex + 1, dialogEvent.item.name),
                    animalId = dialogEvent.item.id ?: 0,
                    onClose = close,
                    listIdControl = state.listIdControl,
                    onClickItem = { menuItem ->
                        when(menuItem){
                            BioListItemMenu.Savepoint -> {
                                onAction(BioListAction.ShowDialog(BioListDialogEvent.ShowEditSavePoint(dialogEvent.item,dialogEvent.posIndex)))
                            }
                        }
                    }
                )
            }
            is BioListDialogEvent.AskFavoriteDelete -> {
                ShowQuestionDialog(
                    title = stringResource(R.string.question_remove_list_from_favorite),
                    content = stringResource(R.string.affect_current_list),
                    onClosed = close,
                    onApproved = {
                        onAction(BioListAction.AddToFavorite(dialogEvent.animalId))
                    }
                )
            }

            is BioListDialogEvent.ShowEditSavePoint -> {
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
fun BioListPagePreview() {
    BioListPage(
        state = BioListState(),
        onAction = {},
        onNavigateBack = {},
        args = BioListRoute(
            categoryId = 1,
            itemId = 1,
            initPosIndex = 0
        ),
        onNavigateToBioDetail = {},
        pagingItems = getPreviewLazyPagingData(
            items = listOf(SampleDatas.generateSpeciesDetail(id = 1), SampleDatas.generateSpeciesDetail(id = 2), SampleDatas.generateSpeciesDetail(id = 3)),
            sourceLoadStates = previewPagingLoadStates(refresh = LoadState.Loading, append = LoadState.Loading),
        )
    )
}