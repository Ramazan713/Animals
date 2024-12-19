package com.masterplus.animals.features.settings.presentation


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.masterplus.animals.R
import com.masterplus.animals.core.presentation.components.NavigationBackIcon
import com.masterplus.animals.core.presentation.dialogs.ShowLoadingDialog
import com.masterplus.animals.core.shared_features.auth.presentation.AuthAction
import com.masterplus.animals.core.shared_features.auth.presentation.AuthState
import com.masterplus.animals.core.shared_features.auth.presentation.AuthViewModel
import com.masterplus.animals.features.settings.presentation.sections.AdvancedSettingSection
import com.masterplus.animals.features.settings.presentation.sections.CloudBackupSection
import com.masterplus.animals.features.settings.presentation.sections.GeneralSettingSection
import com.masterplus.animals.features.settings.presentation.sections.ProfileSettingSection
import com.masterplus.animals.features.settings.presentation.sections.SettingListeners
import com.masterplus.animals.features.settings.presentation.sections.ShowSettingDialog
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsPageRoot(
    onNavigateBack: () -> Unit,
    onNavigateToLinkedAccounts: () -> Unit,
    onNavigateToSavePointSettings: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val authState by authViewModel.state.collectAsStateWithLifecycle()

    SettingsPage(
        state = state,
        onAction = viewModel::onAction,
        authState = authState,
        onAuthAction = authViewModel::onAction,
        onNavigateBack = onNavigateBack,
        onNavigateToLinkedAccounts = onNavigateToLinkedAccounts,
        onNavigateToSavePointSettings = onNavigateToSavePointSettings
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(
    state: SettingsState,
    onAction: (SettingsAction) -> Unit,
    authState: AuthState,
    onAuthAction: (AuthAction) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToLinkedAccounts: () -> Unit,
    onNavigateToSavePointSettings: () -> Unit
) {
    val topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    SettingListeners(
        state = state,
        onEvent = onAction,
        authState = authState,
        onAuthEvent = onAuthAction
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.settings)) },
                navigationIcon = { NavigationBackIcon(onNavigateBack = onNavigateBack) },
                scrollBehavior = topAppBarScrollBehavior
            )
        },
    ){paddings->
        LazyColumn(
            modifier = Modifier
                .padding(paddings)
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            item() {
                ProfileSettingSection(
                    user = authState.user,
                    onAction = onAction
                )
            }

            item {
                GeneralSettingSection(
                    state = state,
                    onAction = onAction
                )
            }

            if(authState.user != null){
                item {
                    CloudBackupSection(onAction)
                }
            }

            item {
                AdvancedSettingSection(
                    state = state,
                    onEvent = onAction,
                    user = authState.user,
                    onNavigateToLinkedAccounts = onNavigateToLinkedAccounts,
                    onNavigateToSavePointSettings = onNavigateToSavePointSettings
                )
            }

            if(authState.user != null){
                item {
                    TextButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp),
                        onClick = {
                            onAction(SettingsAction.ShowDialog(SettingsDialogEvent.AskSignOut))
                        }
                    ) {
                        Text(
                            text = stringResource(R.string.sign_out),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = MaterialTheme.colorScheme.error
                            )
                        )
                    }
                }
            }
        }
    }

    state.dialogEvent?.let { dialogEvent->
        ShowSettingDialog(
            state = state,
            onAction = onAction,
            onAuthAction = onAuthAction,
            authState = authState,
            dialogEvent = dialogEvent
        )
    }

    if(state.isLoading || authState.isLoading){
        ShowLoadingDialog()
    }
}


@Preview(showBackground = true)
@Composable
private fun SettingsPagePreview() {
//    SettingsPage(
//        state = SettingsState(),
//        onAction = {},
//        onNavigateBack = {}
//    )
}