package com.masterplus.animals.features.settings.presentation.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.masterplus.animals.core.presentation.utils.ShowLifecycleToastMessage
import com.masterplus.animals.core.shared_features.auth.presentation.AuthAction
import com.masterplus.animals.core.shared_features.auth.presentation.AuthState
import com.masterplus.animals.features.settings.presentation.SettingsAction
import com.masterplus.animals.features.settings.presentation.SettingsState


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

    ShowLifecycleToastMessage(
        message = state.message,
        onDismiss = { onEvent(SettingsAction.ClearMessage) }
    )

    ShowLifecycleToastMessage(
        message = authState.message,
        onDismiss = { onAuthEvent(AuthAction.ClearMessage) }
    )
}
