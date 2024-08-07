package com.masterplus.animals.features.settings.presentation.link_accounts

import com.masterplus.animals.core.shared_features.auth.domain.enums.AuthProviderType

sealed interface LinkAccountsDialogEvent {

    data object ShowLinkWithEmailDia: LinkAccountsDialogEvent

    data class ShowAlertUnlinkProvider(
        val providerType: AuthProviderType
    ): LinkAccountsDialogEvent
}