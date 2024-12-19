package com.masterplus.animals.core.shared_features.auth.presentation

sealed interface AuthUiAction {

    data object ShowBackupSectionForLogin: AuthUiAction

    data object RefreshApp: AuthUiAction
}