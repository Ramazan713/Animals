package com.masterplus.animals.features.settings.presentation.sections

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import com.masterplus.animals.core.extentions.refreshApp
import com.masterplus.animals.core.presentation.utils.EventHandler
import com.masterplus.animals.core.presentation.utils.ShowLifecycleToastMessage
import com.masterplus.animals.core.shared_features.auth.presentation.AuthAction
import com.masterplus.animals.core.shared_features.auth.presentation.AuthState
import com.masterplus.animals.core.shared_features.auth.presentation.AuthUiAction
import com.masterplus.animals.features.settings.presentation.SettingsAction
import com.masterplus.animals.features.settings.presentation.SettingsDialogEvent
import com.masterplus.animals.features.settings.presentation.SettingsState


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
fun SettingListeners(
    state: SettingsState,
    onEvent: (SettingsAction)->Unit,
    authState: AuthState,
    onAuthEvent: (AuthAction) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(true) {
        onEvent(SettingsAction.LoadData)
    }

    EventHandler(authState.uiAction) { action ->
        onAuthEvent(AuthAction.ClearUiAction)
        when (action) {
            AuthUiAction.RefreshApp -> {
                context.refreshApp()
            }
            AuthUiAction.ShowBackupSectionForLogin -> {
                onEvent(
                    SettingsAction.ShowDialog(
                        SettingsDialogEvent.BackupSectionInit {
                            onAuthEvent(AuthAction.LoadLastBackup)
                        })
                )
            }
        }
    }

    ShowLifecycleToastMessage(
        message = state.message,
        onDismiss = { onEvent(SettingsAction.ClearMessage) }
    )

    ShowLifecycleToastMessage(
        message = authState.message,
        onDismiss = { onAuthEvent(AuthAction.ClearMessage) }
    )
}
