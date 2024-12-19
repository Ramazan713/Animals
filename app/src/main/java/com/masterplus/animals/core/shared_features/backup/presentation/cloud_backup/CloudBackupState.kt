package com.masterplus.animals.core.shared_features.backup.presentation.cloud_backup

import com.masterplus.animals.core.domain.utils.UiText


data class CloudBackupState(
    val isLoading: Boolean = false,
    val message: UiText? = null
)
