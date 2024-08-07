package com.masterplus.animals.core.shared_features.auth.presentation

sealed interface AuthDialogEvent {
    data class EnterEmailForResetPassword(
        val email: String,
        val onResult: (String) -> Unit
    ): AuthDialogEvent
}