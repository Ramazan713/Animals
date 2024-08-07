package com.masterplus.animals.core.shared_features.auth.presentation

import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.shared_features.auth.domain.models.User

data class AuthState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val message: UiText? = null,
    val dialogEvent: AuthDialogEvent? = null,
)
