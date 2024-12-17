package com.masterplus.animals.features.list.presentation.archive_list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
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
import com.masterplus.animals.core.presentation.components.NavigationBackIcon
import com.masterplus.animals.core.presentation.components.loading.SharedLoadingPageContent
import com.masterplus.animals.core.presentation.dialogs.ShowGetTextDialog
import com.masterplus.animals.core.presentation.dialogs.ShowQuestionDialog
import com.masterplus.animals.core.presentation.selections.ShowSelectBottomMenuItems
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.core.presentation.utils.ShowLifecycleToastMessage
import com.masterplus.animals.core.shared_features.list.domain.models.ListView
import com.masterplus.animals.features.list.domain.enums.ArchiveListBottomMenuEnum
import com.masterplus.animals.features.list.presentation.components.ListViewItem
import org.koin.androidx.compose.koinViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun ArchiveListPageRoot(
    onNavigateBack: () -> Unit,
    onNavigateToDetailList: (listId: Int) -> Unit,
    viewModel: ArchiveListViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ArchiveListPage(
        state = state,
        onAction = viewModel::onAction,
        onNavigateToDetailList = onNavigateToDetailList,
        onNavigateBack = onNavigateBack
    )
}

@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun ArchiveListPage(
    onNavigateBack: ()->Unit,
    onNavigateToDetailList: (listId: Int)->Unit,
    state: ArchiveListState,
    onAction: (ArchiveListAction) -> Unit,
){
    val topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current

    ShowLifecycleToastMessage(
        message = state.message,
        onDismiss = { onAction(ArchiveListAction.ClearMessage) }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.archive_n)) },
                scrollBehavior = topAppBarScrollBehavior,
                navigationIcon = { NavigationBackIcon(onNavigateBack = onNavigateBack) }
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
            emptyMessage = stringResource(R.string.archive_empty_text)
        ){
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
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
                                    onAction(ArchiveListAction.ShowDialog(
                                        dialogEvent = ArchiveListDialogEvent.ShowListBottomMenu(
                                            items = ArchiveListBottomMenuEnum.entries,
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


private fun handleMenuItem(
    menuItem: ArchiveListBottomMenuEnum,
    onAction: (ArchiveListAction) -> Unit,
    listView: ListView
){
    when(menuItem){
        ArchiveListBottomMenuEnum.Delete->{
            onAction(ArchiveListAction.ShowDialog(ArchiveListDialogEvent.AskDelete(listView),))
        }
        ArchiveListBottomMenuEnum.Rename -> {
            onAction(ArchiveListAction.ShowDialog(ArchiveListDialogEvent.Rename(listView),))
        }
        ArchiveListBottomMenuEnum.UnArchive -> {
            onAction(ArchiveListAction.ShowDialog(ArchiveListDialogEvent.AskUnArchive(listView),))
        }
    }
}


@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
private fun ShowDialog(
    event: ArchiveListDialogEvent?,
    onAction: (ArchiveListAction)->Unit
){
    val close = remember {{
        onAction(ArchiveListAction.ShowDialog(null))
    }}

    when(event){
        is ArchiveListDialogEvent.AskDelete -> {
            ShowQuestionDialog(
                title = stringResource(R.string.question_delete),
                content = stringResource(R.string.not_revertable),
                onApproved = {onAction(ArchiveListAction.Delete(event.listView))},
                onClosed = close
            )
        }
        null -> {}
        is ArchiveListDialogEvent.AskUnArchive -> {
            ShowQuestionDialog(
                title = stringResource(R.string.question_unarchive),
                onApproved = {onAction(ArchiveListAction.UnArchive(event.listView))},
                onClosed = close
            )
        }
        is ArchiveListDialogEvent.Rename -> {
            ShowGetTextDialog(
                title = stringResource(R.string.rename),
                content = event.listView.name,
                onClosed = close,
                onApproved = {onAction(ArchiveListAction.Rename(event.listView,it))}
            )
        }
        is ArchiveListDialogEvent.ShowListBottomMenu -> {
            ShowSelectBottomMenuItems(
                items = event.items,
                title = event.title,
                onClickItem = { menuItem ->
                    handleMenuItem(menuItem,onAction,event.listView)
                },
                onClose = close
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class
)
@Preview
@Composable
private fun ArchiveListPagePreview(modifier: Modifier = Modifier) {
    ArchiveListPage(
        state = ArchiveListState(
            items = listOf(SampleDatas.listView, SampleDatas.listView.copy(id = 3))
        ),
        onAction = {},
        onNavigateToDetailList = {},
        onNavigateBack = {}
    )
}