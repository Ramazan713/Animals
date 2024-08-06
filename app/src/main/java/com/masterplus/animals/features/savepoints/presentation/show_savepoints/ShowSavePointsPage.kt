package com.masterplus.animals.features.savepoints.presentation.show_savepoints


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.masterplus.animals.R
import com.masterplus.animals.core.presentation.components.NavigationBackIcon
import com.masterplus.animals.core.presentation.components.SharedLoadingPageContent
import com.masterplus.animals.core.presentation.dialogs.ShowGetTextDialog
import com.masterplus.animals.core.presentation.dialogs.ShowQuestionDialog
import com.masterplus.animals.core.presentation.selections.CustomDropdownMenu
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.core.presentation.utils.ShowLifecycleToastMessage
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointItemMenu
import com.masterplus.animals.core.shared_features.savepoint.domain.models.SavePoint
import com.masterplus.animals.core.shared_features.savepoint.presentation.components.SavePointItem
import com.masterplus.animals.core.shared_features.savepoint.presentation.components.SavePointItemDefaults
import org.koin.androidx.compose.koinViewModel

@Composable
fun ShowSavePointsPageRoot(
    onNavigateBack: () -> Unit,
    onNavigateToBioList: (SavePoint) -> Unit,
    viewModel: ShowSavePointsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ShowLifecycleToastMessage(
        message = state.message,
        onDismiss = { viewModel.onAction(ShowSavePointsAction.ClearMessage) }
    )

    ShowSavePointsPage(
        state = state,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack,
        onNavigateToBioList = onNavigateToBioList
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun ShowSavePointsPage(
    state: ShowSavePointsState,
    onAction: (ShowSavePointsAction) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToBioList: (SavePoint) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "SavePoints") },
                scrollBehavior = scrollBehavior,
                navigationIcon = { NavigationBackIcon(onNavigateBack) }
            )
        }
    ) { paddings ->

        Column(
            modifier = Modifier
                .padding(paddings)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(horizontal = 8.dp)
                .fillMaxSize(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                if(state.showDropdownMenu){
                    CustomDropdownMenu(
                        items = state.dropdownItems,
                        currentItem = state.selectedDropdownItem,
                        onItemChange = { onAction(ShowSavePointsAction.SelectDropdownMenuItem(it)) }
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        state.currentSelectedSavePoint?.let { savePoint ->
                            onNavigateToBioList(savePoint)
                        }
                    },
                    enabled = state.currentSelectedSavePoint != null
                ) {
                    Text(text = stringResource(R.string.load_and_go))
                }
            }

            SharedLoadingPageContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                isLoading = state.isLoading,
                isEmptyResult = state.savePoints.isEmpty(),
                overlayLoading = true,
                emptyMessage = stringResource(id = R.string.empty_savepoint)
            ) {
                LazyColumn(
                    modifier = Modifier.matchParentSize(),
                    contentPadding = PaddingValues(bottom = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        state.savePoints,
                        key = {item -> item.id ?: 0}
                    ){item->
                        SavePointItem(
                            modifier = Modifier.fillMaxWidth(),
                            savePoint = item,
                            isSelected = item == state.currentSelectedSavePoint,
                            itemDefaults = SavePointItemDefaults(
                                showImage = state.showImage,
                            ),
                            onClick = {
                                onAction(ShowSavePointsAction.Select(item))
                            },
                            onMenuClick = { menuItem ->
                                when(menuItem){
                                    SavePointItemMenu.Rename -> {
                                        onAction(
                                            ShowSavePointsAction.ShowDialog(ShowSavePointsDialogEvent.EditTitle(item))
                                        )
                                    }
                                    SavePointItemMenu.Delete -> {
                                        onAction(
                                            ShowSavePointsAction.ShowDialog(ShowSavePointsDialogEvent.AskDelete(item))
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    state.dialogEvent?.let { dialogEvent ->
        ShowDialog(
            event = dialogEvent,
            onAction = onAction,
        )
    }
    
}



@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
private fun ShowDialog(
    event: ShowSavePointsDialogEvent,
    onAction: (ShowSavePointsAction)->Unit,
){
    val close = remember {{
        onAction(ShowSavePointsAction.ShowDialog(null))
    }}
    when(event){
        is ShowSavePointsDialogEvent.AskDelete -> {
            ShowQuestionDialog(
                title = stringResource(R.string.question_delete),
                content = stringResource(R.string.not_revertable),
                onApproved = {onAction(ShowSavePointsAction.Delete(event.savePoint))},
                onClosed = close
            )
        }
        is ShowSavePointsDialogEvent.EditTitle -> {
            ShowGetTextDialog(
                title = stringResource(R.string.rename),
                content = event.savePoint.title,
                onApproved = {onAction(ShowSavePointsAction.EditTitle(it, event.savePoint))},
                onClosed = close
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
private fun ShowSavePointsPagePreview() {
    ShowSavePointsPage(
        state = ShowSavePointsState(
            savePoints = listOf(SampleDatas.generateSavePoint())
        ),
        onAction = {},
        onNavigateBack = {},
        onNavigateToBioList = {x->}
    )
}