package com.masterplus.animals.core.shared_features.backup.presentation.backup_select

import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.shared_features.backup.domain.models.BackupMeta

data class SelectBackupState(
    val items: List<BackupMeta> = emptyList(),
    val selectedItem: BackupMeta? = null,
    val isRefreshEnabled: Boolean = true,
    val refreshSeconds: Int = 0,
    val isLoading: Boolean = false,
    val showDialog: Boolean = false,
    val dialogEvent: BackupSelectDialogEvent? = null,
    val uiEvent: BackupSelectUiEvent? = null,
    val message: UiText? = null
)
