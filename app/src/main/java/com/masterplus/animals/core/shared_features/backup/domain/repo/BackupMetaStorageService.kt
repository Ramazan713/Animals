package com.masterplus.animals.core.shared_features.backup.domain.repo

import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.domain.utils.EmptyDefaultResult
import com.masterplus.animals.core.shared_features.backup.domain.models.BackupMeta

interface BackupMetaStorageService {

    suspend fun getFiles(userId: String): DefaultResult<List<BackupMeta>>

    suspend fun getFileData(userId: String, fileName: String): DefaultResult<ByteArray>

    suspend fun deleteFile(userId: String, fileName: String): EmptyDefaultResult

    suspend fun uploadData(userId: String, fileName: String, data: ByteArray): EmptyDefaultResult
}