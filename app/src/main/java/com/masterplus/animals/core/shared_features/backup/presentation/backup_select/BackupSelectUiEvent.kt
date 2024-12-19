package com.masterplus.animals.core.shared_features.backup.presentation.backup_select

sealed class BackupSelectUiEvent{
    data object RestartApp: BackupSelectUiEvent()
}
