package com.masterplus.animals.core.shared_features.backup.domain.manager

import com.masterplus.animals.core.domain.utils.EmptyDefaultResult

interface BackupManager {

    suspend fun uploadBackup(userId: String): EmptyDefaultResult

    suspend fun downloadBackup(userId: String, fileName: String, removeAllData: Boolean, addOnLocalData: Boolean): EmptyDefaultResult

    suspend fun refreshBackupMetas(userId: String): EmptyDefaultResult

    suspend fun deleteAllLocalUserData(deleteBackupMeta: Boolean)

    suspend fun downloadLastBackup(userId: String): EmptyDefaultResult

    suspend fun hasBackupMetas(): Boolean
}