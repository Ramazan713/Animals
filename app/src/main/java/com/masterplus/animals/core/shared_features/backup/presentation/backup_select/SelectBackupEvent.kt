package com.masterplus.animals.core.shared_features.backup.presentation.backup_select

import com.masterplus.animals.core.shared_features.backup.domain.models.BackupMeta


sealed class SelectBackupEvent{
    data object Refresh: SelectBackupEvent()

    data class SelectItem(val backupMeta: BackupMeta): SelectBackupEvent()

    data object OverrideBackup: SelectBackupEvent()

    data object AddTopOfBackup: SelectBackupEvent()

    data class ShowDialog(val showDialog: Boolean, val dialogEvent: BackupSelectDialogEvent? = null ): SelectBackupEvent()

    data object ClearUiEvent: SelectBackupEvent()

    data object ClearMessage: SelectBackupEvent()
}
