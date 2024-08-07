package com.masterplus.animals.features.settings.presentation.link_accounts

import com.google.firebase.auth.AuthCredential
import com.masterplus.animals.core.shared_features.auth.domain.enums.AuthProviderType

sealed interface LinkAccountsAction {

    data class LinkWithEmail(
        val email: String,
        val password: String
    ): LinkAccountsAction

    data class LinkWith(val credential: AuthCredential): LinkAccountsAction

    data class UnLinkWith(val providerType: AuthProviderType): LinkAccountsAction

    data class ShowDialog(val dialogEvent: LinkAccountsDialogEvent?): LinkAccountsAction

    data object ClearMessage: LinkAccountsAction

}