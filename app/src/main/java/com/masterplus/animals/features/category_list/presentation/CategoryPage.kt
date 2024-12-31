package com.masterplus.animals.features.category_list.presentation


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.masterplus.animals.R
import com.masterplus.animals.core.data.mediators.RemoteMediatorError
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.domain.models.ImageWithMetadata
import com.masterplus.animals.core.extentions.isAppendItemLoading
import com.masterplus.animals.core.extentions.isEmptyResult
import com.masterplus.animals.core.extentions.isLoading
import com.masterplus.animals.core.extentions.visibleMiddlePosition
import com.masterplus.animals.core.presentation.components.DefaultTopBar
import com.masterplus.animals.core.presentation.components.TopBarType
import com.masterplus.animals.core.presentation.components.image.ImageWithTitle
import com.masterplus.animals.core.presentation.components.loading.SharedCircularProgress
import com.masterplus.animals.core.presentation.components.loading.SharedLoadingLazyVerticalGrid
import com.masterplus.animals.core.presentation.components.paging.AppendErrorHandlerComponent
import com.masterplus.animals.core.presentation.components.paging.PagingEmptyComponent
import com.masterplus.animals.core.presentation.components.paging.PrependErrorHandlerComponent
import com.masterplus.animals.core.presentation.selections.ShowSelectBottomMenuItems
import com.masterplus.animals.core.presentation.transition.TransitionImageKey
import com.masterplus.animals.core.presentation.transition.TransitionImageType
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.core.presentation.utils.getPreviewLazyPagingData
import com.masterplus.animals.core.presentation.utils.previewPagingLoadStates
import com.masterplus.animals.core.shared_features.savepoint.data.mapper.toSavePointDestinationTypeId
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import com.masterplus.animals.core.shared_features.savepoint.domain.models.EditSavePointLoadParam
import com.masterplus.animals.core.shared_features.savepoint.presentation.auto_savepoint.AutoSavePointAction
import com.masterplus.animals.core.shared_features.savepoint.presentation.auto_savepoint.AutoSavePointHandler
import com.masterplus.animals.core.shared_features.savepoint.presentation.auto_savepoint.AutoSavePointState
import com.masterplus.animals.core.shared_features.savepoint.presentation.edit_savepoint.EditSavePointDialog
import com.masterplus.animals.features.category_list.domain.enums.CategoryListBottomItemMenu
import com.masterplus.animals.features.category_list.domain.enums.CategoryListTopBarItemMenu


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun CategoryListPage(
    state: CategoryState,
    pagingItems: LazyPagingItems<CategoryData>,
    onAction: (CategoryAction) -> Unit,
    onAutoSavePointAction: (AutoSavePointAction) -> Unit,
    autoSavePointState: AutoSavePointState,
    onDestination: () -> SavePointDestination,
    onNavigateBack: () -> Unit,
    onAllItemClick: () -> Unit,
    onItemClick: (CategoryData) -> Unit,
    onNavigateToCategorySearch: () -> Unit,
    onNavigateToSavePointCategorySettings: () -> Unit,
    initPos: Int = 0
) {
    val topBarScrollBehaviour = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val lazyListState = rememberLazyGridState()
    val middlePos = lazyListState.visibleMiddlePosition()

    AutoSavePointHandler(
        contentType = SavePointContentType.Category,
        onDestination = onDestination,
        onAction = onAutoSavePointAction,
        state = autoSavePointState,
        itemInitPos = initPos,
        lazyListState = lazyListState,
        topBarScrollBehaviour = topBarScrollBehaviour,
        pagingItems = pagingItems
    )

    Scaffold(
        topBar = {
            GetTopBar(
                topBarScrollBehaviour = topBarScrollBehaviour,
                onNavigateBack = onNavigateBack,
                title = state.title,
                subTitle = state.subTitle,
                onNavigateToCategorySearch = onNavigateToCategorySearch,
                onAction = onAction,
                listMiddlePos = { middlePos },
                onNavigateToSavePointCategorySettings = onNavigateToSavePointCategorySettings
            )
        }
    ) { paddings->
        SharedLoadingLazyVerticalGrid(
            modifier = Modifier
                .padding(paddings)
                .fillMaxSize()
                .nestedScroll(topBarScrollBehaviour.nestedScrollConnection),
            isEmptyResult = pagingItems.isEmptyResult(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
            state = lazyListState,
            isLoading = pagingItems.isLoading() || autoSavePointState.loadingSavePointPos || state.isLoading,
            emptyContent = {
                PagingEmptyComponent(
                    pagingItems = pagingItems,
                    onWatchAd = {
                        onAutoSavePointAction(AutoSavePointAction.ShowAd)
                    }
                )
            },
            stickHeaderContent = {
                item(
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    HeaderImage(
                        state = state,
                        image = state.parentImageData,
                        onAllItemClick = onAllItemClick,
                        showImage = !state.isLoading,
                        modifier = Modifier
                    )
                }
                item(
                    span = { GridItemSpan(maxLineSpan) }
                ) {
                    Text(
                        text = state.collectionName,
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
            }
        ) {
            PrependErrorHandlerComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                ,
                pagingItems = pagingItems,
                onWatchAd = {
                    onAutoSavePointAction(AutoSavePointAction.ShowAd)
                }
            )

            items(
                count = pagingItems.itemCount,
                key = pagingItems.itemKey { it.id },
                contentType = pagingItems.itemContentType { "MyPagingCategoryPage" }
            ){ index ->
                val item = pagingItems[index]
                if(item != null){
                    ImageWithTitle(
                        modifier = Modifier
                            .fillMaxWidth(),
                        model = item,
                        order = index + 1,
                        useTransition = true,
                        onClick = {
                            onItemClick(item)
                        },
                        onLongClick = {
                            onAction(CategoryAction.ShowDialog(
                                dialogEvent = CategoryListDialogEvent.ShowBottomSheet(
                                    item = item
                                )
                            ))
                        }
                    )
                }
            }

            AppendErrorHandlerComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                ,
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

    state.dialogEvent?.let { dialogEvent ->
        val close = remember {{
            onAction(CategoryAction.ShowDialog(null))
        }}
        when(dialogEvent){
            is CategoryListDialogEvent.ShowEditSavePoint -> {
                EditSavePointDialog(
                    loadParam = EditSavePointLoadParam(
                        destinationTypeId = state.categoryType.toSavePointDestinationTypeId(state.categoryItemId, returnAll = false),
                        destinationId = state.categoryItemId,
                        kingdomType = state.kingdomType,
                        contentType = SavePointContentType.Category,
                    ),
                    itemId = dialogEvent.itemId,
                    onClosed = close,
                    onNavigateLoad = { savepoint ->
                        onAction(CategoryAction.SetPagingTargetId(savepoint.itemId))
                        onAutoSavePointAction(AutoSavePointAction.RequestNavigateToPosByItemId(
                            itemId = savepoint.itemId,
                        ))
                    }
                )
            }

            is CategoryListDialogEvent.ShowBottomSheet -> {
                ShowSelectBottomMenuItems(
                    items = CategoryListBottomItemMenu.entries,
                    title = stringResource(R.string.n_for_number_word, dialogEvent.item.id + 1, dialogEvent.item.title),
                    onClose = close,
                    onClickItem = { menuItem ->
                        when(menuItem){
                            CategoryListBottomItemMenu.Savepoint -> {
                                onAction(CategoryAction.ShowDialog(CategoryListDialogEvent.ShowEditSavePoint(
                                    itemId = dialogEvent.item.id
                                )))
                            }
                        }
                    }
                )
            }
        }
    }

}

@Composable
private fun HeaderImage(
    state: CategoryState,
    image: ImageWithMetadata?,
    showImage: Boolean,
    onAllItemClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val extraKey by remember(state, image) {
        derivedStateOf {
            if(state.showAllImageInHeader) "all"
            else if(image == null) "default"
            else null
        }
    }
    AnimatedVisibility(
        enter = fadeIn() + expandIn(),
        visible = showImage,
        modifier = modifier
    ) {
        ImageWithTitle(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            title = "${if(state.kingdomType.isAnimals) "Hayvanlar" else "Bitkiler"} Listesi",
            image = image,
            fallbackImageData = R.drawable.animals_plants,
            onClick = onAllItemClick,
            useTransition = true,
            transitionKey = TransitionImageType.fromCategoryType(state.categoryType)
                ?.let {
                    TransitionImageKey(
                        id = state.categoryItemId ?: 0,
                        kingdomType = state.kingdomType,
                        imageType = it,
                        extra = extraKey
                    )
                }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GetTopBar(
    topBarScrollBehaviour: TopAppBarScrollBehavior,
    onAction: (CategoryAction) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToCategorySearch: () -> Unit,
    onNavigateToSavePointCategorySettings: () -> Unit,
    title: String,
    subTitle: String?,
    listMiddlePos: () -> Int
) {
    DefaultTopBar(
        topBarType = TopBarType.LARGE,
        title = {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                )
                if(!subTitle.isNullOrBlank() && topBarScrollBehaviour.state.collapsedFraction <= 0.4f ){
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = subTitle,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        },
        onNavigateBack = onNavigateBack,
        scrollBehavior = topBarScrollBehaviour,
        actions = {
            IconButton(onClick = onNavigateToCategorySearch) {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }
        },
        menuItems = CategoryListTopBarItemMenu.entries,
        onMenuItemClick = { menuItem ->
            when(menuItem){
                CategoryListTopBarItemMenu.Savepoint -> {
                    onAction(CategoryAction.ShowDialog(CategoryListDialogEvent.ShowEditSavePoint(
                        itemId = listMiddlePos()
                    )))
                }
                CategoryListTopBarItemMenu.SavePointSettings -> onNavigateToSavePointCategorySettings()
            }
        }
    )
}



@Preview(showBackground = true)
@Composable
private fun CategoryListPagePreview1() {
    CategoryListPage(
        state = CategoryState(
            kingdomType = KingdomType.Animals,
            categoryItemId = 1,
            categoryType = CategoryType.Class,
            parentImageData = SampleDatas.imageWithMetadata,
            title = "Sample title",
            subTitle = "SubTitle",
            isLoading = false
        ),
        onAction = {},
        pagingItems = getPreviewLazyPagingData<CategoryData>(
            items = listOf(
                SampleDatas.categoryData, SampleDatas.categoryData.copy(id = 5)
            ),
            sourceLoadStates = previewPagingLoadStates(
                prepend = LoadState.Error(RemoteMediatorError.NoInternetConnectionException)
            ),
//            mediatorLoadStates = previewPagingLoadStates(append = LoadState.Loading)
        ),
        onNavigateBack = {

        },
        onItemClick = {},
        onAllItemClick = {},
        onNavigateToCategorySearch = {},
        autoSavePointState = AutoSavePointState(),
        onAutoSavePointAction = {},
        onDestination = {SampleDatas.sampleDestination},
        onNavigateToSavePointCategorySettings = {}
    )
}

//@Preview(showBackground = true)
//@Composable
//fun CategoryListPagePreview2() {
//    CategoryListPage(
//        state = CategoryListState(),
//        onAction = {},
//        items = getPreviewLazyPagingData(
//            items = listOf(
//                SampleDatas.imageWithTitleModel1
//            ),
//            sourceLoadStates = previewPagingLoadStates(
//                append = LoadState.Loading
//            )
//        )
//    )
//}