package com.masterplus.animals.features.settings.presentation.link_accounts.models

import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.shared_features.auth.domain.enums.AuthProviderType

data class LinkAccountModel(
    val title: UiText,
    val isConnected: Boolean,
    val providerType: AuthProviderType,
    val info: UiText? = null
)
