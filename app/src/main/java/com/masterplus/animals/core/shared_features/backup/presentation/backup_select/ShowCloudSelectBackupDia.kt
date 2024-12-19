package com.masterplus.animals.core.shared_features.backup.presentation.backup_select

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.masterplus.animals.R
import com.masterplus.animals.core.extentions.refreshApp
import com.masterplus.animals.core.presentation.components.SharedHeader
import com.masterplus.animals.core.presentation.dialogs.BaseDialog
import com.masterplus.animals.core.presentation.dialogs.LoadingDialog
import com.masterplus.animals.core.presentation.dialogs.ShowQuestionDialog
import com.masterplus.animals.core.presentation.utils.EventHandler
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.core.presentation.utils.ShowLifecycleToastMessage
import com.masterplus.animals.core.shared_features.backup.domain.models.BackupMeta
import com.masterplus.animals.features.settings.presentation.components.SelectableText
import com.masterplus.animals.features.settings.presentation.components.TextIcon
import org.koin.androidx.compose.koinViewModel

@Composable
fun ShowCloudSelectBackupDia(
    onClosed: ()->Unit,
    selectBackupViewModel: CloudSelectBackupViewModel = koinViewModel(),
){
    val state by selectBackupViewModel.state.collectAsStateWithLifecycle()
    ShowCloudSelectBackupDia(
        onClosed = onClosed,
        state = state,
        onEvent = selectBackupViewModel::onEvent,
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class
)
@Composable
fun ShowCloudSelectBackupDia(
    onClosed: () -> Unit,
    state: SelectBackupState,
    onEvent: (SelectBackupEvent) -> Unit,
){
    val context = LocalContext.current

    ShowLifecycleToastMessage(
        message = state.message,
        onDismiss = { onEvent(SelectBackupEvent.ClearMessage) }
    )

    EventHandler(event = state.uiEvent) {event->
        when(event){
            BackupSelectUiEvent.RestartApp -> {
                context.refreshApp()
            }
        }
    }

    BaseDialog(
        onClosed = onClosed,
    ){
        Column(
            modifier = Modifier.padding(horizontal = 13.dp, vertical = 6.dp)
        ) {
            SharedHeader(
                content = {
                    TextIcon(
                        title = stringResource(R.string.download_from_cloud),
                        imageVector = Icons.Default.CloudDownload
                    )
                },
                onIconClick = onClosed
            )
            LazyColumn(
                modifier = Modifier
            ) {

                item {
                    GetRefreshInfo(state = state, onEvent = onEvent)
                }

                if(state.items.isEmpty()){
                    item {
                        Text(
                            stringResource(R.string.empty_backup_select),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                        )
                    }
                }

                items(
                    state.items,
                    key = {item -> item.id ?: 0}
                ){item->
                    SelectableText(
                        title = item.title,
                        isSelected = item == state.selectedItem,
                        onClick = {
                            onEvent(SelectBackupEvent.SelectItem(item))
                        },
                        modifier = Modifier
                            .padding(vertical = 3.dp)
                            .fillMaxWidth()
                    )
                }
            }

            GetButtons(
                onEvent = onEvent,
                selectedItem = state.selectedItem
            )
        }
    }

    if(state.showDialog){
        ShowDialog(
            event = state.dialogEvent,
            onEvent = {onEvent(it)}
        )
    }

    if(state.isLoading){
        LoadingDialog()
    }
}


@Composable
private fun GetRefreshInfo(
    state: SelectBackupState,
    onEvent: (SelectBackupEvent) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        TextButton(
            onClick = { onEvent(SelectBackupEvent.Refresh) },
            enabled = state.isRefreshEnabled,
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = stringResource(id = R.string.refresh)
            )
            Text(
                text = stringResource(id = R.string.refresh),
                modifier = Modifier.padding(start = 2.dp)
            )
        }

        if(!state.isRefreshEnabled){
            Text(state.refreshSeconds.toString(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.error
                )
            )
        }
    }
}

@Composable
private fun GetButtons(
    onEvent: (SelectBackupEvent) -> Unit,
    selectedItem: BackupMeta?
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            enabled = selectedItem != null,
            onClick = {
                onEvent(
                    SelectBackupEvent.ShowDialog(
                        true,
                        BackupSelectDialogEvent.AskAddOnBackup
                    )
                )
            },
            modifier = Modifier.weight(1f)
        ) {
            Text(text = stringResource(R.string.add_on))
        }

        Button(
            enabled = selectedItem != null,
            onClick = {
                onEvent(
                    SelectBackupEvent.ShowDialog(
                        true,
                        BackupSelectDialogEvent.AskOverrideBackup
                    )
                )
            },
            modifier = Modifier.weight(1f)
        ) {
            Text(text = stringResource(R.string.override))
        }
    }
}


@Composable
fun ShowDialog(
    event: BackupSelectDialogEvent?,
    onEvent: (SelectBackupEvent)->Unit
){
    fun close(){
        onEvent(SelectBackupEvent.ShowDialog(false))
    }

    when(event){
        BackupSelectDialogEvent.AskAddOnBackup -> {
            ShowQuestionDialog(
                title = stringResource(R.string.are_sure_to_continue),
                content = stringResource(R.string.add_on_backup_warning),
                onClosed = { close() },
                onApproved = { onEvent(SelectBackupEvent.AddTopOfBackup) }
            )
        }
        BackupSelectDialogEvent.AskOverrideBackup -> {
            ShowQuestionDialog(
                title = stringResource(R.string.are_sure_to_continue),
                content = stringResource(R.string.override_backup_warning),
                onClosed = { close() },
                onApproved = { onEvent(SelectBackupEvent.OverrideBackup) }
            )
        }
        null -> {}
    }

}


@Preview(showBackground = true)
@Composable
fun ShowCloudSelectBackupPreview() {
    ShowCloudSelectBackupDia(
        onClosed = {},
        onEvent = {},
        state = SelectBackupState(
            items = listOf(
                SampleDatas.backupMeta,
                SampleDatas.backupMeta.copy(id = 2)
            ),
            isRefreshEnabled = false,
            refreshSeconds = 10
        )
    )
}

@Preview(showBackground = true)
@Composable
fun ShowCloudSelectBackupPreview2() {
    ShowCloudSelectBackupDia(
        onClosed = {},
        onEvent = {},
        state = SelectBackupState()
    )
}

