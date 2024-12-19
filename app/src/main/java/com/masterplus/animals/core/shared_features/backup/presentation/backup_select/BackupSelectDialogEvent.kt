package com.masterplus.animals.core.shared_features.backup.presentation.backup_select

sealed class BackupSelectDialogEvent{

    data object AskOverrideBackup: BackupSelectDialogEvent()

    data object AskAddOnBackup: BackupSelectDialogEvent()
}
