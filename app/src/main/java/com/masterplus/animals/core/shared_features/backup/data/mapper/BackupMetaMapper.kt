package com.masterplus.animals.core.shared_features.backup.data.mapper

import com.google.firebase.storage.StorageMetadata
import com.masterplus.animals.core.shared_features.backup.domain.models.BackupMeta
import com.masterplus.animals.core.shared_features.database.entity.BackupMetaEntity

fun BackupMetaEntity.toBackupMeta(): BackupMeta {
    return BackupMeta(
        id = id,
        fileName = fileName,
        updatedDate = updatedDate,
        title = "Backup - "
    )
}

fun BackupMeta.toBackupMetaEntity(): BackupMetaEntity {
    return BackupMetaEntity(
        id = id,
        fileName = fileName,
        updatedDate = updatedDate,
    )
}

fun StorageMetadata.toBackupMeta(): BackupMeta {
    return BackupMeta(
        fileName = name?:"",
        updatedDate = updatedTimeMillis,
        id = null,
        title = "Backup - "
    )
}