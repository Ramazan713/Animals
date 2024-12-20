package com.masterplus.animals.features.list.presentation.show_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.masterplus.animals.R
import com.masterplus.animals.core.extentions.isScrollingUp
import com.masterplus.animals.core.presentation.components.DefaultToolTip
import com.masterplus.animals.core.presentation.components.DefaultTopBar
import com.masterplus.animals.core.presentation.components.loading.SharedLoadingPageContent
import com.masterplus.animals.core.presentation.defaults.SettingTopBarMenuEnum
import com.masterplus.animals.core.presentation.dialogs.ShowGetTextDialog
import com.masterplus.animals.core.presentation.dialogs.ShowQuestionDialog
import com.masterplus.animals.core.presentation.selections.ShowSelectBottomMenuItems
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.core.presentation.utils.ShowLifecycleToastMessage
import com.masterplus.animals.core.shared_features.list.domain.models.ListView
import com.masterplus.animals.features.list.domain.enums.ShowListBottomMenuEnum
import com.masterplus.animals.features.list.presentation.components.ListViewItem
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowListPageRoot(
    onNavigateToArchive: ()->Unit,
    onNavigateToDetailList: (listId: Int)->Unit,
    onNavigateToSettings: ()-> Unit,
    viewModel: ShowListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ShowListPage(
        state = state,
        onAction = viewModel::onAction,
        onNavigateToDetailList = onNavigateToDetailList,
        onNavigateToArchive = onNavigateToArchive,
        onNavigateToSettings = onNavigateToSettings
    )
}



@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@ExperimentalMaterial3Api
@Composable
fun ShowListPage(
    onNavigateToArchive: ()-> Unit,
    onNavigateToSettings: ()-> Unit,
    onNavigateToDetailList: (listId: Int)->Unit,
    state: ShowListState,
    onAction: (ShowListAction) -> Unit,
){

    val lazyGridState = rememberLazyGridState()
    val topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val isScrollingUp = lazyGridState.isScrollingUp()
    val context = LocalContext.current

    ShowLifecycleToastMessage(
        message = state.message,
        onDismiss = { onAction(ShowListAction.ClearMessage) }
    )

    Scaffold(
        topBar = {
            GetTopBar(
                topAppBarScrollBehavior = topAppBarScrollBehavior,
                onNavigateToArchive = onNavigateToArchive,
                onNavigateToSettings = onNavigateToSettings,
            )
        },
        floatingActionButton = {
            GetFab(
                isScrollingUp = isScrollingUp,
                onAction = onAction,
            )
        },
    ){paddings->
        SharedLoadingPageContent(
            modifier = Modifier
                .padding(paddings)
                .fillMaxSize()
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
            isLoading = state.isLoading,
            isEmptyResult = state.items.isEmpty(),
            overlayLoading = true,
            emptyMessage = stringResource(R.string.list_empty_text)
        ) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                state = lazyGridState,
                columns = GridCells.Adaptive(300.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                contentPadding = PaddingValues(horizontal = 8.dp),
                content = {
                    items(
                        state.items,
                        key = {item -> item.id ?: 0}
                    ){item->
                        ListViewItem(
                            listView = item,
                            onClick = {
                                onNavigateToDetailList(item.id?:0)
                            },
                            trailingItem = {
                                IconButton(onClick = {
                                    onAction(ShowListAction.ShowDialog(
                                        dialogEvent = ShowListDialogEvent.ShowListBottomMenu(
                                            items = ShowListBottomMenuEnum.from(item.isRemovable),
                                            title = context.getString(R.string.n_for_list_item, item.name),
                                            listView = item
                                        )
                                    ))
                                }) {
                                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = stringResource(id = R.string.menu))
                                }
                            },
                        )
                    }
                }
            )
        }
    }

    state.dialogEvent?.let { dialogEvent ->
        ShowDialog(
            event = dialogEvent,
            onAction = onAction
        )
    }
}

@Composable
private fun GetFab(
    isScrollingUp: Boolean,
    onAction: (ShowListAction) -> Unit,
) {
    AnimatedVisibility(
        visible = isScrollingUp,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        ExtendedFloatingActionButton(
            icon = { Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_list)) },
            text = { Text(text = stringResource(id = R.string.add))},
            expanded = false,
            onClick = { onAction(ShowListAction.ShowDialog(ShowListDialogEvent.TitleToAddList)) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GetTopBar(
    topAppBarScrollBehavior: TopAppBarScrollBehavior,
    onNavigateToArchive: () -> Unit,
    onNavigateToSettings: () -> Unit,
) {
    DefaultTopBar(
        title = stringResource(id = R.string.list),
        menuItems = SettingTopBarMenuEnum.entries,
        scrollBehavior = topAppBarScrollBehavior,
        onMenuItemClick = { menuItem->
            when(menuItem){
                SettingTopBarMenuEnum.Settings -> onNavigateToSettings()
            }
        },
        actions = {
            DefaultToolTip(
                tooltip = stringResource(id = R.string.archive_n),
                spacingBetweenTooltipAndAnchor = 8.dp,
            ) {
                IconButton(onClick = onNavigateToArchive){
                    Icon(
                        imageVector = Icons.Default.Archive,
                        contentDescription = stringResource(id = R.string.archive_n)
                    )
                }
            }
        },
    )
}


private fun handleSelectMenuItem(
    menuEnum: ShowListBottomMenuEnum?,
    onAction: (ShowListAction)->Unit,
    listView: ListView
){
    when(menuEnum){
        ShowListBottomMenuEnum.Delete->{
            onAction(ShowListAction.ShowDialog(ShowListDialogEvent.AskDelete(listView)))
        }
        ShowListBottomMenuEnum.Rename -> {
            onAction(ShowListAction.ShowDialog(ShowListDialogEvent.Rename(listView)))
        }
        ShowListBottomMenuEnum.Archive -> {
            onAction(ShowListAction.ShowDialog(ShowListDialogEvent.AskArchive(listView)))
        }
        ShowListBottomMenuEnum.Copy -> {
            onAction(ShowListAction.ShowDialog(ShowListDialogEvent.AskCopy(listView)))
        }
        null -> {}
    }
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
private fun ShowDialog(
    event: ShowListDialogEvent?,
    onAction: (ShowListAction) -> Unit
){
    val close = remember {{
        onAction(ShowListAction.ShowDialog(null))
    }}

    when(event){
        is ShowListDialogEvent.AskDelete -> {
            ShowQuestionDialog(
                title = stringResource(R.string.question_delete),
                content = stringResource(R.string.not_revertable),
                onApproved = {onAction(ShowListAction.Delete(event.listView))},
                onClosed = close
            )
        }
        is ShowListDialogEvent.TitleToAddList -> {
            ShowGetTextDialog(
                title = stringResource(R.string.enter_title),
                onClosed = close,
                onApproved = {onAction(ShowListAction.AddNewList(it))}
            )
        }
        null -> {}
        is ShowListDialogEvent.AskArchive -> {
            ShowQuestionDialog(
                title = stringResource(R.string.question_archive_title),
                content = stringResource(R.string.question_archive_content),
                onApproved = {onAction(ShowListAction.Archive(event.listView))},
                onClosed = close
            )
        }
        is ShowListDialogEvent.AskCopy -> {
            ShowQuestionDialog(
                title = stringResource(R.string.question_copy),
                onApproved = {onAction(ShowListAction.Copy(event.listView))},
                onClosed = close
            )
        }
        is ShowListDialogEvent.Rename -> {
            ShowGetTextDialog(
                title = stringResource(R.string.rename),
                content = event.listView.name,
                onClosed = close,
                onApproved = {onAction(ShowListAction.Rename(event.listView,it))}
            )
        }
        is ShowListDialogEvent.ShowListBottomMenu -> {
            ShowSelectBottomMenuItems(
                items = event.items,
                title = event.title,
                onClickItem = { menuItem ->
                    handleSelectMenuItem(menuItem,onAction,event.listView)
                },
                onClose = close
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun ShowListPagePreview() {
    ShowListPage(
        state = ShowListState(
            items = listOf(SampleDatas.listView, SampleDatas.listView.copy(id = 3))
        ),
        onAction = {},
        onNavigateToDetailList = {},
        onNavigateToArchive = {},
        onNavigateToSettings = {}
    )
}




