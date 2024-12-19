package com.masterplus.animals.features.settings.presentation.sections

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.masterplus.animals.R
import com.masterplus.animals.features.settings.presentation.SettingsAction
import com.masterplus.animals.features.settings.presentation.SettingsDialogEvent
import com.masterplus.animals.features.settings.presentation.components.SettingItem
import com.masterplus.animals.features.settings.presentation.components.SettingSectionItem

@Composable
fun CloudBackupSection(
    onAction: (SettingsAction)->Unit,
){
    SettingSectionItem(
        title = stringResource(R.string.backup_n),
    ){
        SettingItem(
            title = stringResource(R.string.cloud_backup),
            onClick = {onAction(SettingsAction.ShowDialog(SettingsDialogEvent.ShowCloudBackup))},
            imageVector = Icons.Default.Cloud,
        )
    }
}