
package com.masterplus.animals.core.shared_features.savepoint.presentation.edit_savepoint

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.masterplus.animals.R
import com.masterplus.animals.core.presentation.components.SharedHeader
import com.masterplus.animals.core.presentation.dialogs.BaseDialog
import com.masterplus.animals.core.presentation.dialogs.ShowGetTextDialog
import com.masterplus.animals.core.presentation.dialogs.ShowQuestionDialog
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.core.presentation.utils.ShowLifecycleToastMessage
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointItemMenu
import com.masterplus.animals.core.shared_features.savepoint.domain.models.EditSavePointLoadParam
import com.masterplus.animals.core.shared_features.savepoint.domain.models.SavePoint
import com.masterplus.animals.core.shared_features.savepoint.presentation.components.SavePointItem
import com.masterplus.animals.core.shared_features.savepoint.presentation.components.SavePointItemDefaults
import org.koin.androidx.compose.koinViewModel


@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun EditSavePointDialog(
    loadParam: EditSavePointLoadParam,
    posIndex: Int,
    onClosed: () -> Unit,
    onNavigateLoad: (SavePoint) -> Unit,
    editViewModel: EditSavePointViewModel = koinViewModel()
){
    val state by editViewModel.state.collectAsStateWithLifecycle()
    EditSavePointDialog(
        loadParam = loadParam,
        posIndex = posIndex,
        onClosed = onClosed,
        onNavigateLoad = onNavigateLoad,
        state = state,
        onAction = editViewModel::onAction
    )
}

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
fun EditSavePointDialog(
    loadParam: EditSavePointLoadParam,
    posIndex: Int,
    onClosed: () -> Unit,
    onNavigateLoad: (SavePoint) -> Unit,
    state: EditSavePointState,
    onAction: (EditSavePointAction) -> Unit
){
    LaunchedEffect(loadParam){
        onAction(EditSavePointAction.LoadData(loadParam))
    }

    ShowLifecycleToastMessage(
        message = state.message,
        onDismiss = { onAction(EditSavePointAction.ClearMessage) }
    )

    BaseDialog(
        onClosed = onClosed,
        usePlatformDefaultWidth = false
    ){
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 2.dp)
        ){
            SharedHeader(
                title = stringResource(R.string.save_points_c),
                onIconClick = onClosed,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 7.dp)
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                ,
                contentPadding = PaddingValues(bottom = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                item {
                    OutlinedButton(
                        onClick = {
                            onAction(EditSavePointAction.RequestAddNewSavePoint)
                        },
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.add_new_savepoint))
                    }
                }
                if(state.savePoints.isEmpty()){
                    item {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 24.dp)
                        ) {
                            Text(
                                stringResource(R.string.empty_savepoint),
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }else{
                    items(
                        state.savePoints,
                        key = {item -> item.id ?: 0}
                    ){item->
                        SavePointItem(
                            modifier = Modifier.fillMaxWidth(),
                            savePoint = item,
                            isSelected = item == state.currentSelectedSavePoint,
                            itemDefaults = SavePointItemDefaults(
                                showImage = state.showImage
                            ),
                            onClick = {
                                onAction(EditSavePointAction.Select(item))
                            },
                            onMenuClick = { menuItem ->
                                when(menuItem){
                                    SavePointItemMenu.Rename -> {
                                        onAction(
                                            EditSavePointAction.ShowDialog(EditSavePointDialogEvent.EditTitle(item))
                                        )
                                    }
                                    SavePointItemMenu.Delete -> {
                                        onAction(
                                            EditSavePointAction.ShowDialog(EditSavePointDialogEvent.AskDelete(item))
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Button(
                    onClick = { onAction(EditSavePointAction.OverrideSavePoint(posIndex)) },
                    modifier = Modifier.weight(1f),
                    enabled = state.currentSelectedSavePoint != null,
                ) {
                    Text(text = stringResource(R.string.override),)
                }
                Button(
                    onClick = {
                        state.currentSelectedSavePoint?.let { savePoint ->
                            onNavigateLoad(savePoint)
                            onClosed()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = state.currentSelectedSavePoint != null,
                ) {
                    Text(text = stringResource(R.string.load))
                }
            }
        }

        state.dialogEvent?.let { dialogEvent ->
            ShowDialog(
                event = dialogEvent,
                onAction = onAction,
                posIndex = posIndex
            )
        }
    }

}

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable
private fun ShowDialog(
    posIndex: Int,
    event: EditSavePointDialogEvent,
    onAction: (EditSavePointAction)->Unit,
){
    val close = remember {{
        onAction(EditSavePointAction.ShowDialog(null))
    }}
    when(event){
        is EditSavePointDialogEvent.AskDelete -> {
            ShowQuestionDialog(
                title = stringResource(R.string.question_delete),
                content = stringResource(R.string.not_revertable),
                onApproved = {onAction(EditSavePointAction.Delete(event.savePoint))},
                onClosed = close
            )
        }
        is EditSavePointDialogEvent.EditTitle -> {
            ShowGetTextDialog(
                title = stringResource(R.string.rename),
                content = event.savePoint.title,
                onApproved = {onAction(EditSavePointAction.EditTitle(it, event.savePoint))},
                onClosed = close
            )
        }
        is EditSavePointDialogEvent.AddSavePointTitle -> {
            ShowGetTextDialog(
                title = stringResource(R.string.enter_title),
                content = event.suggestedTitle.asString(),
                onApproved = { newTitle ->
                    onAction(EditSavePointAction.AddSavePoint(
                        title = newTitle,
                        currentDateTime = event.currentDateTime,
                        posIndex = posIndex
                    ))
                },
                onClosed = close
            )
        }
    }
}



@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Preview(showBackground = true)
@Composable
private fun EditSavePointPagePreview() {
    EditSavePointDialog(
        loadParam = EditSavePointLoadParam(
            destinationTypeId = 1,
            destinationId = null
        ),
        posIndex = 1,
        onClosed = {  },
        onNavigateLoad = {  },
        state = EditSavePointState(savePoints = listOf(
            SampleDatas.generateSavePoint(), SampleDatas.generateSavePoint(id = 2)
        )),
        onAction = {  }
    )
}