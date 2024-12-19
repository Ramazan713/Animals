package com.masterplus.animals.core.shared_features.backup.domain.models

data class BackupMeta(
    val id: Int?,
    val fileName: String,
    val updatedDate: Long,
    val title: String = ""
)
