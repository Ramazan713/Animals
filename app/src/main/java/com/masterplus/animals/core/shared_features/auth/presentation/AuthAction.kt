package com.masterplus.animals.core.shared_features.auth.presentation

import com.google.firebase.auth.AuthCredential

sealed interface AuthAction {

    data class SignInWithEmail(val email: String, val password: String): AuthAction

    data class SignUpWithEmail(val email: String, val password: String): AuthAction

    data class SignInWithCredential(val credential: AuthCredential): AuthAction

    data class ResetPassword(val email: String): AuthAction

    data object ClearMessage: AuthAction

    data class ShowDialog(val dialogEvent: AuthDialogEvent?): AuthAction

    data class SignOut(val backupBeforeSignOut: Boolean): AuthAction

    data class DeleteUserWithCredentials(val credential: AuthCredential): AuthAction

    data class DeleteUserWithEmail(val email: String, val password: String): AuthAction

    data object DeleteAllUserData: AuthAction

    data object ClearUiAction: AuthAction

    data object LoadLastBackup: AuthAction

}