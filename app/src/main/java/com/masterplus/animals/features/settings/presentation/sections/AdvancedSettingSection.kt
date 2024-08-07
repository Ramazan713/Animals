package com.masterplus.animals.features.settings.presentation.sections

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Login
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.masterplus.animals.R
import com.masterplus.animals.core.shared_features.auth.domain.models.User
import com.masterplus.animals.features.settings.presentation.SettingsAction
import com.masterplus.animals.features.settings.presentation.SettingsDialogEvent
import com.masterplus.animals.features.settings.presentation.SettingsState
import com.masterplus.animals.features.settings.presentation.components.SettingItem
import com.masterplus.animals.features.settings.presentation.components.SettingSectionItem


@Composable
fun AdvancedSettingSection(
    state: SettingsState,
    onEvent: (SettingsAction)->Unit,
    user: User?,
    onNavigateToLinkedAccounts: () -> Unit
){
    SettingSectionItem(
        title = stringResource(R.string.advanced_setting),
    ){

        AnimatedVisibility(visible = user != null) {
            SettingItem(
                title = "Login Options",
                imageVector = Icons.AutoMirrored.Filled.Login,
                onClick = onNavigateToLinkedAccounts
            )
        }

        AnimatedVisibility(visible = user != null) {
            SettingItem(
                title = stringResource(R.string.delete_account),
                imageVector = Icons.Default.AccountCircle,
                color = MaterialTheme.colorScheme.error,
                onClick = {
                    onEvent(SettingsAction.ShowDialog(SettingsDialogEvent.AskDeleteAccount))
                }
            )
        }

    }
}