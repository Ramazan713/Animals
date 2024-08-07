package com.masterplus.animals.features.settings.presentation.link_accounts

import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.features.settings.presentation.link_accounts.models.LinkAccountModel

data class LinkAccountsState(
    val isLoading: Boolean = false,
    val linkedAccounts: List<LinkAccountModel> = emptyList(),
    val unLinkedAccounts: List<LinkAccountModel> = emptyList(),
    val message: UiText? = null,
    val dialogEvent: LinkAccountsDialogEvent? = null,
)
