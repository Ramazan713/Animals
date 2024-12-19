package com.masterplus.animals.core.shared_features.backup.domain.repo

import com.masterplus.animals.core.shared_features.backup.domain.models.BackupMeta
import kotlinx.coroutines.flow.Flow

interface BackupMetaRepo {

    suspend fun insertBackupMetas(backupMetas: List<BackupMeta>)

    suspend fun getLastBackupMeta(): BackupMeta?

    fun getBackupMetasFlow(): Flow<List<BackupMeta>>

    suspend fun deleteBackupMetas()

    suspend fun deleteBackupMetas(backupMetas: List<BackupMeta>)

    suspend fun hasBackupMetas(): Boolean

    suspend fun getExtraBackupMetas(offset: Int): List<BackupMeta>
}