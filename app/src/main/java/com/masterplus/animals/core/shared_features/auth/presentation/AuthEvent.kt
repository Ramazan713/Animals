package com.masterplus.animals.core.shared_features.auth.presentation

import com.google.firebase.auth.AuthCredential
import com.masterplus.animals.core.domain.utils.UiText

sealed interface AuthEvent {

    data class SignInWithEmail(val email: String, val password: String): AuthEvent

    data class SignUpWithEmail(val email: String, val password: String): AuthEvent

    data class SignInWithCredential(val credential: AuthCredential): AuthEvent

    data class ResetPassword(val email: String): AuthEvent

    data object ClearMessage: AuthEvent

    data class ShowDialog(val dialogEvent: AuthDialogEvent?): AuthEvent

    data class SignOut(val backupBeforeSignOut: Boolean): AuthEvent

    data class DeleteUserWithCredentials(val credential: AuthCredential): AuthEvent

    data class DeleteUserWithEmail(val email: String, val password: String): AuthEvent
}