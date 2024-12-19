package com.masterplus.animals.core.shared_features.backup.presentation.cloud_backup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.masterplus.animals.R
import com.masterplus.animals.core.presentation.components.SharedHeader
import com.masterplus.animals.core.presentation.dialogs.BaseDialog
import com.masterplus.animals.core.presentation.dialogs.LoadingDialog
import com.masterplus.animals.core.presentation.dialogs.ShowQuestionDialog
import com.masterplus.animals.core.presentation.utils.ShowLifecycleToastMessage
import com.masterplus.animals.core.shared_features.backup.presentation.backup_select.ShowCloudSelectBackupDia
import com.masterplus.animals.features.settings.presentation.components.TextIcon
import org.koin.androidx.compose.koinViewModel


@Composable
fun ShowCloudSetting(
    onClosed: ()->Unit,
    cloudViewModel: CloudBackupViewModel = koinViewModel()
){
    val state by cloudViewModel.state.collectAsStateWithLifecycle()
    ShowCloudSetting(
        onClosed = onClosed,
        onMakeBackup = cloudViewModel::makeBackup,
        onClearMessage = cloudViewModel::clearMessage,
        state = state,
    )
}

@Composable
fun ShowCloudSetting(
    onClosed: ()->Unit,
    state: CloudBackupState,
    onMakeBackup: () -> Unit,
    onClearMessage: () -> Unit
){

    var isVisibleSelectBackupDialog by rememberSaveable{
        mutableStateOf(false)
    }
    var isVisibleAddBackupDialog by rememberSaveable{
        mutableStateOf(false)
    }

    ShowLifecycleToastMessage(
        message = state.message,
        onDismiss = onClearMessage
    )


    BaseDialog(
        onClosed = onClosed,
    ){
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(vertical = 16.dp, horizontal = 2.dp)
        ) {
            item {
                SharedHeader(
                    content = {
                        TextIcon(
                            title = stringResource(R.string.cloud_backup),
                           imageVector = Icons.Default.Cloud
                        )
                    },
                    onIconClick = onClosed
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 8.dp)
                        .padding(horizontal = 8.dp)
                    ,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            isVisibleAddBackupDialog = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.add_backup))
                    }

                    Button(
                        onClick = {
                            isVisibleSelectBackupDialog = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.download_from_cloud))
                    }
                }
            }
        }
    }

    if(state.isLoading){
        LoadingDialog()
    }else if(isVisibleSelectBackupDialog){
        ShowCloudSelectBackupDia(
            onClosed = { isVisibleSelectBackupDialog = false},
        )
    }else if(isVisibleAddBackupDialog){
        ShowQuestionDialog(
            title = stringResource(R.string.are_sure_to_continue),
            content = stringResource(R.string.some_backup_files_may_change),
            onClosed = { isVisibleAddBackupDialog = false },
            onApproved = onMakeBackup
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ShowCloudSettingPreview() {
    ShowCloudSetting(
        onClosed = {},
        state = CloudBackupState(),
        onClearMessage = {},
        onMakeBackup = {}
    )
}

