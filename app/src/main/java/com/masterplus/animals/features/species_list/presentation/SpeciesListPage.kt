package com.masterplus.animals.features.species_list.presentation


import androidx.compose.animation.ExperimentalSharedTransitionApi
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.masterplus.animals.core.data.mediators.RemoteMediatorError
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.ContentType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.SpeciesListDetail
import com.masterplus.animals.core.extentions.isAppendItemLoading
import com.masterplus.animals.core.extentions.isEmptyResult
import com.masterplus.animals.core.extentions.isLoading
import com.masterplus.animals.core.extentions.isPrependItemLoading
import com.masterplus.animals.core.extentions.visibleMiddleItemOrderKey
import com.masterplus.animals.core.extentions.visibleMiddlePosition
import com.masterplus.animals.core.presentation.components.DefaultTopBar
import com.masterplus.animals.core.presentation.components.SpeciesCard
import com.masterplus.animals.core.presentation.components.SpeciesCardShimmer
import com.masterplus.animals.core.presentation.components.loading.SharedCircularProgress
import com.masterplus.animals.core.presentation.components.loading.SharedLoadingPageContent
import com.masterplus.animals.core.presentation.components.paging.AppendErrorHandlerComponent
import com.masterplus.animals.core.presentation.components.paging.PagingEmptyComponent
import com.masterplus.animals.core.presentation.components.paging.PrependErrorHandlerComponent
import com.masterplus.animals.core.presentation.transition.animateEnterExitForTransition
import com.masterplus.animals.core.presentation.transition.renderInSharedTransitionScopeOverlayDefault
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.core.presentation.utils.getPreviewLazyPagingData
import com.masterplus.animals.core.presentation.utils.previewPagingLoadStates
import com.masterplus.animals.core.shared_features.ad.presentation.AdAction
import com.masterplus.animals.core.shared_features.ad.presentation.AdState
import com.masterplus.animals.core.shared_features.add_species_to_list.presentation.AddSpeciesToListAction
import com.masterplus.animals.core.shared_features.add_species_to_list.presentation.AddSpeciesToListDialogEvent
import com.masterplus.animals.core.shared_features.add_species_to_list.presentation.AddSpeciesToListHandler
import com.masterplus.animals.core.shared_features.add_species_to_list.presentation.AddSpeciesToListState
import com.masterplus.animals.core.shared_features.add_species_to_list.presentation.AddSpeciesToListViewModel
import com.masterplus.animals.core.shared_features.savepoint.data.mapper.toSavePointDestinationTypeId
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import com.masterplus.animals.core.shared_features.savepoint.domain.models.EditSavePointLoadParam
import com.masterplus.animals.core.shared_features.savepoint.presentation.auto_savepoint.AutoSavePointAction
import com.masterplus.animals.core.shared_features.savepoint.presentation.auto_savepoint.AutoSavePointHandler
import com.masterplus.animals.core.shared_features.savepoint.presentation.auto_savepoint.AutoSavePointState
import com.masterplus.animals.core.shared_features.savepoint.presentation.auto_savepoint.AutoSavePointViewModel
import com.masterplus.animals.core.shared_features.savepoint.presentation.edit_savepoint.EditSavePointDialog
import com.masterplus.animals.core.shared_features.select_font_size.presentation.ShowSelectFontSizeDia
import com.masterplus.animals.features.species_list.domain.enums.SpeciesListBottomItemMenu
import com.masterplus.animals.features.species_list.domain.enums.SpeciesListTopItemMenu
import com.masterplus.animals.features.species_list.presentation.navigation.SpeciesListRoute
import org.koin.androidx.compose.koinViewModel


@Composable
fun SpeciesListPageRoot(
    viewModel: SpeciesListViewModel = koinViewModel(),
    addSpeciesToListViewModel: AddSpeciesToListViewModel = koinViewModel(),
    autoSavePointViewModel: AutoSavePointViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToSpeciesDetail: (Int, Int?) -> Unit,
    onNavigateToCategorySearch: (CategoryType, ContentType, KingdomType, Int?) -> Unit,
    onNavigateToSavePointSpeciesSettings: () -> Unit,
    adState: AdState,
    onAdAction: (AdAction) -> Unit,
) {
    val args = viewModel.args
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pagingItems = viewModel.pagingItems.collectAsLazyPagingItems()

    val addSpeciesState by addSpeciesToListViewModel.state.collectAsStateWithLifecycle()
    val autoSavePointState by autoSavePointViewModel.state.collectAsStateWithLifecycle()

    SpeciesListPage(
        state = state,
        onAction = viewModel::onAction,
        addSpeciesState = addSpeciesState,
        onAddSpeciesAction = addSpeciesToListViewModel::onAction,
        args = args,
        onNavigateBack = onNavigateBack,
        onNavigateToSpeciesDetail = onNavigateToSpeciesDetail,
        pagingItems = pagingItems,
        onAutoSavePointAction = autoSavePointViewModel::onAction,
        onNavigateToCategorySearch = {
            onNavigateToCategorySearch(args.categoryType, ContentType.Category, args.kingdomType, args.categoryItemId)
        },
        autoSavePointState = autoSavePointState,
        onNavigateToSavePointSpeciesSettings = onNavigateToSavePointSpeciesSettings,
        onAdAction = onAdAction,
        adState = adState
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class, ExperimentalSharedTransitionApi::class
)
@Composable
fun SpeciesListPage(
    pagingItems: LazyPagingItems<SpeciesListDetail>,
    state: SpeciesListState,
    onAction: (SpeciesListAction) -> Unit,
    onAutoSavePointAction: (AutoSavePointAction) -> Unit,
    autoSavePointState: AutoSavePointState,
    addSpeciesState: AddSpeciesToListState,
    onAddSpeciesAction: (AddSpeciesToListAction) -> Unit,
    args: SpeciesListRoute,
    onNavigateBack: () -> Unit,
    onNavigateToSpeciesDetail: (Int, Int?) -> Unit,
    onNavigateToCategorySearch: () -> Unit,
    onNavigateToSavePointSpeciesSettings: () -> Unit,
    adState: AdState,
    onAdAction: (AdAction) -> Unit,
) {
    val topBarScrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val lazyListState = rememberLazyListState()
    val middleOrderKey = pagingItems.visibleMiddleItemOrderKey(lazyListState)
    val middlePos = lazyListState.visibleMiddlePosition()

    AutoSavePointHandler(
        contentType = SavePointContentType.Content,
        onDestination = {SavePointDestination.fromCategoryType(
            args.categoryType,
            args.categoryItemId,
            kingdomType = args.kingdomType
        )},
        onAction = onAutoSavePointAction,
        state = autoSavePointState,
        itemInitPos = args.initPosIndex,
        topBarScrollBehaviour = topBarScrollBehaviour,
        lazyListState = lazyListState,
        pagingItems = pagingItems,
        onAdAction = onAdAction,
        adState = adState
    )


    Scaffold(
        topBar = {
            DefaultTopBar(
                scrollBehavior = topBarScrollBehaviour,
                title = state.title?.asString() ?: "",
                onNavigateBack = onNavigateBack,
                menuItems = SpeciesListTopItemMenu.entries,
                onMenuItemClick = { menuItem ->
                    when(menuItem){
                        SpeciesListTopItemMenu.Savepoint -> {
                            onAction(SpeciesListAction.ShowDialog(middleOrderKey?.let { middleOrderKey->
                                SpeciesListDialogEvent.ShowEditSavePoint(
                                    orderKey = middleOrderKey,
                                    posIndex = middlePos
                                )
                            }))
                        }
                        SpeciesListTopItemMenu.SavePointSettings -> onNavigateToSavePointSpeciesSettings()
                        SpeciesListTopItemMenu.SelectFontSize -> {
                            onAction(SpeciesListAction.ShowDialog(SpeciesListDialogEvent.ShowSelectFontSize))
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToCategorySearch) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    }
                },
                modifier = Modifier
                    .renderInSharedTransitionScopeOverlayDefault()
                    .animateEnterExitForTransition()
            )
        }
    ) { paddings ->

        SharedLoadingPageContent(
            modifier = Modifier
                .padding(paddings)
                .nestedScroll(topBarScrollBehaviour.nestedScrollConnection)
                .fillMaxSize(),
            isLoading = pagingItems.isLoading() || autoSavePointState.loadingSavePointPos,
            isEmptyResult = pagingItems.isEmptyResult(),
            emptyContent = {
                PagingEmptyComponent(
                    pagingItems = pagingItems,
                    onWatchAd = {
                        onAutoSavePointAction(AutoSavePointAction.ShowAd)
                    }
                )
            },
            overlayLoading = true
        ) {
            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp)
            ) {

                if(pagingItems.isPrependItemLoading()){
                    item {
                        SharedCircularProgress(modifier = Modifier.fillMaxWidth())
                    }
                }

                PrependErrorHandlerComponent(
                    modifier = Modifier.fillMaxWidth(),
                    pagingItems = pagingItems,
                    onWatchAd = {
                        onAutoSavePointAction(AutoSavePointAction.ShowAd)
                    }
                )

                items(
                    count = pagingItems.itemCount,
                    key = pagingItems.itemKey { it.id },
                    contentType = pagingItems.itemContentType { "MyPagingSpeciesList" }
                ){ index ->
                    val item = pagingItems[index]
                    if(item != null){
                        SpeciesCard(
                            species = item,
                            orderNum = "${item.orderKey}",
                            isFavorited = item.isFavorited,
                            onClick = {
                                onNavigateToSpeciesDetail(item.id, state.listIdControl)
                            },
                            onFavoriteClick = {
                                onAddSpeciesAction(AddSpeciesToListAction.AddToFavorite(item.id))
                            },
                            onUnFavoriteClick = {
                                onAddSpeciesAction(AddSpeciesToListAction.AddOrAskFavorite(item.id))
                            },
                            onMenuButtonClick = {
                                onAddSpeciesAction(AddSpeciesToListAction.ShowDialog(AddSpeciesToListDialogEvent.ShowItemBottomMenu(
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
                    pagingItems = pagingItems,
                    onWatchAd = {
                        onAutoSavePointAction(AutoSavePointAction.ShowAd)
                    }
                )

                if(pagingItems.isAppendItemLoading()){
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
        bottomMenuItems = SpeciesListBottomItemMenu.entries,
        onBottomMenuItemClick = { menuItem, orderKey, pos ->
            when(menuItem){
                SpeciesListBottomItemMenu.Savepoint -> {
                    onAction(SpeciesListAction.ShowDialog(SpeciesListDialogEvent.ShowEditSavePoint(pos, orderKey)))
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
                        destinationTypeId = args.categoryType.toSavePointDestinationTypeId(args.categoryItemId),
                        destinationId = args.categoryItemId,
                        kingdomType = args.kingdomType
                    ),
                    orderKey = dialogEvent.orderKey,
                    onClosed = close,
                    onNavigateLoad = { savepoint ->
                        onAction(SpeciesListAction.SetPagingTargetId(savepoint.orderKey))
                        onAutoSavePointAction(AutoSavePointAction.RequestNavigateToPosByItemId(
                            orderKey = savepoint.orderKey,
                        ))
                    }
                )
            }

            SpeciesListDialogEvent.ShowSelectFontSize -> {
                ShowSelectFontSizeDia(onClose = close)
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
            categoryType = CategoryType.Class,
            categoryItemId = 1,
            initPosIndex = 0,
            kingdomType = KingdomType.Animals
        ),
        onNavigateToSpeciesDetail = {x,y ->},
        pagingItems = getPreviewLazyPagingData(
            items = listOf(
                SampleDatas.generateSpeciesDetail(id = 1),
//                SampleDatas.generateSpeciesDetail(id = 2),
//                SampleDatas.generateSpeciesDetail(id = 3)
            ),
            sourceLoadStates = previewPagingLoadStates(
                refresh = LoadState.Error(RemoteMediatorError.ReadLimitExceededException),
//                append = LoadState.Error(RemoteMediatorError.ReadLimitExceededException)
            ),
        ),
        onNavigateToCategorySearch = {},
        onAutoSavePointAction = {},
        autoSavePointState = AutoSavePointState(),
        onNavigateToSavePointSpeciesSettings = {},
        adState = AdState(),
        onAdAction = {}
    )
}