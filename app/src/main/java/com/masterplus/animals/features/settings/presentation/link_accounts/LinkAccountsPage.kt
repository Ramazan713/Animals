package com.masterplus.animals.features.settings.presentation.link_accounts


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.auth.OAuthProvider
import com.masterplus.animals.R
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.presentation.components.NavigationBackIcon
import com.masterplus.animals.core.presentation.dialogs.ShowLoadingDialog
import com.masterplus.animals.core.presentation.dialogs.ShowQuestionDialog
import com.masterplus.animals.core.presentation.utils.SampleDatas
import com.masterplus.animals.core.presentation.utils.ShowLifecycleToastMessage
import com.masterplus.animals.core.shared_features.auth.domain.enums.AuthProviderType
import com.masterplus.animals.core.shared_features.auth.presentation.utils.AuthProviderUtils
import com.masterplus.animals.features.settings.presentation.link_accounts.components.LinkAccountItemRow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun LinkAccountsPageRoot(
    onNavigateBack: () -> Unit,
    viewModel: LinkAccountsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LinkAccountsPage(
        state = state,
        onAction = viewModel::onAction,
        onNavigateBack = onNavigateBack
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinkAccountsPage(
    state: LinkAccountsState,
    onAction: (LinkAccountsAction) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val credentialManager = remember {
        CredentialManager.create(context)
    }
    var error by remember {
        mutableStateOf<UiText?>(null)
    }

    ShowLifecycleToastMessage(
        message = error
    ) {
        error = null
    }

    ShowLifecycleToastMessage(
        message = state.message
    ) {
        onAction(LinkAccountsAction.ClearMessage)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Login Options") },
                navigationIcon = { NavigationBackIcon(onNavigateBack = onNavigateBack) }
            )
        }
    ) { paddings ->
        LazyColumn(
            modifier = Modifier
                .padding(paddings)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {
                Text(
                    text = "Linked Accounts",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            items(state.linkedAccounts){linkedAccount ->
                LinkAccountItemRow(
                    linkAccountModel = linkedAccount,
                    isButtonEnabled = state.linkedAccounts.size > 1,
                    onUnLinkClick = {
                        onAction(LinkAccountsAction.ShowDialog(
                            LinkAccountsDialogEvent.ShowAlertUnlinkProvider(linkedAccount.providerType)
                        ))
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }


            item {
                Text(
                    text = "UnLinked Accounts",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            items(state.unLinkedAccounts){unLinkedAccount ->
                LinkAccountItemRow(
                    linkAccountModel = unLinkedAccount,
                    onLinkClick = {
                        when(unLinkedAccount.providerType){
                            AuthProviderType.Email -> {
                                onAction(LinkAccountsAction.ShowDialog(
                                    LinkAccountsDialogEvent.ShowLinkWithEmailDia
                                ))
                            }
                            AuthProviderType.Google -> {
                                scope.launch {
                                    AuthProviderUtils.signInWithGoogle(credentialManager, context).also { result ->
                                        result.getSuccessData?.let { credential ->
                                            onAction(LinkAccountsAction.LinkWith(credential))
                                        }
                                        result.getFailureError?.let { error = it.text }
                                    }
                                }
                            }
                            AuthProviderType.X -> {
                                scope.launch {
                                    val provider =  OAuthProvider.newBuilder(unLinkedAccount.providerType.providerId).build()
                                    AuthProviderUtils.linkWithFirebaseProvider(context, provider).also { result ->
                                        result.getSuccessData?.let { credential ->
                                            onAction(LinkAccountsAction.LinkWith(credential))
                                        }
                                        result.getFailureError?.let { error = it.text }
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    }


    state.dialogEvent?.let { dialogEvent ->
        val close = remember {{
            onAction(LinkAccountsAction.ShowDialog(null))
        }}

        when(dialogEvent){
            LinkAccountsDialogEvent.ShowLinkWithEmailDia -> {
                ShowSignInWithEmailDia(
                    onSignIn = {email, password ->
                        onAction(LinkAccountsAction.LinkWithEmail(email, password))
                    },
                    onClosed = close
                )
            }
            is LinkAccountsDialogEvent.ShowAlertUnlinkProvider -> {
                ShowQuestionDialog(
                    title = "Unlink ${dialogEvent.providerType.title.asString()}",
                    content = stringResource(id = R.string.unlink_account_warning),
                    onApproved = {
                        onAction(LinkAccountsAction.UnLinkWith(dialogEvent.providerType))
                    },
                    onClosed = close
                )
            }
        }
    }

    if(state.isLoading){
        ShowLoadingDialog()
    }

}


@Preview(showBackground = true)
@Composable
private fun LinkAccountsPagePreview() {
    LinkAccountsPage(
        state = LinkAccountsState(
            linkedAccounts = listOf(SampleDatas.generateLinkAccountModel()),
            unLinkedAccounts = listOf(SampleDatas.generateLinkAccountModel(
                providerType = AuthProviderType.Google
            )),
        ),
        onAction = {},
        onNavigateBack = {}
    )
}