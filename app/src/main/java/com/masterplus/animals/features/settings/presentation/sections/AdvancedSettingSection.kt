package com.masterplus.animals.features.settings.presentation.sections

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.SettingsBackupRestore
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.masterplus.animals.R
import com.masterplus.animals.core.presentation.components.DefaultAnimatedVisibility
import com.masterplus.animals.core.shared_features.auth.domain.models.User
import com.masterplus.animals.features.settings.presentation.SettingsAction
import com.masterplus.animals.features.settings.presentation.SettingsDialogEvent
import com.masterplus.animals.features.settings.presentation.SettingsState
import com.masterplus.animals.features.settings.presentation.components.SettingItem
import com.masterplus.animals.features.settings.presentation.components.SettingSectionItem


@Composable
fun AdvancedSettingSection(
    state: SettingsState,
    onAction: (SettingsAction)->Unit,
    user: User?,
    onNavigateToLinkedAccounts: () -> Unit,
    onNavigateToSavePointSettings: () -> Unit
){
    SettingSectionItem(
        title = stringResource(R.string.advanced_setting),
    ){

        DefaultAnimatedVisibility(visible = user != null) {
            SettingItem(
                title = "Login Options",
                imageVector = Icons.AutoMirrored.Filled.Login,
                onClick = onNavigateToLinkedAccounts
            )
        }

        SettingItem(
            title = "Kayıt Noktası Ayarları",
            onClick = onNavigateToSavePointSettings,
            imageVector = Icons.Default.Save,
        )

        SettingItem(
            title = stringResource(R.string.reset_default_setting),
            imageVector = Icons.Default.SettingsBackupRestore,
            onClick = {
                onAction(SettingsAction.ShowDialog(SettingsDialogEvent.AskResetDefault))
            }
        )
        SettingItem(
            title = stringResource(R.string.delete_all_data),
            imageVector = Icons.Default.DeleteForever,
            onClick = {
                onAction(SettingsAction.ShowDialog(SettingsDialogEvent.AskDeleteAllData))
            }
        )

        DefaultAnimatedVisibility(visible = user != null) {
            SettingItem(
                title = stringResource(R.string.delete_account),
                imageVector = Icons.Default.AccountCircle,
                color = MaterialTheme.colorScheme.error,
                onClick = {
                    onAction(SettingsAction.ShowDialog(SettingsDialogEvent.AskDeleteAccount))
                }
            )
        }

    }
}