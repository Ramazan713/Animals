package com.masterplus.animals.core.shared_features.backup.data.manager

import com.masterplus.animals.R
import com.masterplus.animals.core.domain.constants.K
import com.masterplus.animals.core.domain.utils.EmptyDefaultResult
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.domain.utils.safeCall
import com.masterplus.animals.core.presentation.utils.ExceptionUiText
import com.masterplus.animals.core.shared_features.backup.domain.manager.BackupManager
import com.masterplus.animals.core.shared_features.backup.domain.models.BackupMeta
import com.masterplus.animals.core.shared_features.backup.domain.repo.BackupMetaRepo
import com.masterplus.animals.core.shared_features.backup.domain.repo.BackupMetaStorageService
import com.masterplus.animals.core.shared_features.backup.domain.repo.LocalBackupRepo
import java.util.Date
import java.util.UUID

class BackupManagerImpl (
    private val localBackupRepo: LocalBackupRepo,
    private val storageService: BackupMetaStorageService,
    private val backupMetaRepo: BackupMetaRepo,
): BackupManager {

    override suspend fun uploadBackup(userId: String): EmptyDefaultResult {
        return safeCall {
            cutExtraBackupFiles(userId)
            val fileName = UUID.randomUUID().toString()
            val timeStamp = Date().time
            val data = localBackupRepo.getJsonData()?.toByteArray(charset = Charsets.UTF_8) ?: throw Exception()
            val uploadDataResult = storageService.uploadData(userId,fileName,data)
            if(uploadDataResult.isSuccess){
                backupMetaRepo.insertBackupMetas(listOf(BackupMeta(id = null, fileName = fileName,timeStamp)))
            }
        }
    }



    override suspend fun downloadBackup(userId: String, fileName: String, removeAllData: Boolean, addOnLocalData: Boolean): EmptyDefaultResult {
        return safeCall {
            val result = storageService.getFileData(userId, fileName)
            if(result.isSuccess){
                val dataString = result.getSuccessData!!.toString(charset = Charsets.UTF_8)
                localBackupRepo.fromJsonData(dataString,removeAllData, addOnLocalData)
            }
        }
    }

    override suspend fun refreshBackupMetas(userId: String): EmptyDefaultResult {
        return safeCall {
            val result = storageService.getFiles(userId)
            if(result.isSuccess){
                backupMetaRepo.deleteBackupMetas()
                backupMetaRepo.insertBackupMetas(result.getSuccessData!!)
            }
        }
    }

    override suspend fun deleteAllLocalUserData(deleteBackupMeta: Boolean) {
        if(deleteBackupMeta){
            backupMetaRepo.deleteBackupMetas()
        }
        localBackupRepo.deleteAllLocalUserData()
    }

    override suspend fun downloadLastBackup(userId: String): EmptyDefaultResult {
        return safeCall {
            val backupMeta = backupMetaRepo.getLastBackupMeta()
                ?: throw ExceptionUiText(UiText.Resource(R.string.backup_file_not_found))
            downloadBackup(
                userId = userId,
                fileName = backupMeta.fileName,
                removeAllData = true,
                addOnLocalData = false
            )
        }
    }

    override suspend fun hasBackupMetas(): Boolean {
        return backupMetaRepo.hasBackupMetas()
    }

    private suspend fun cutExtraBackupFiles(userId: String){
        val backupMetas = backupMetaRepo.getExtraBackupMetas(K.backupMetaSizeInDb - 1)
        backupMetas.forEach { backupMeta ->
            storageService.deleteFile(userId,backupMeta.fileName)
        }
        backupMetaRepo.deleteBackupMetas(backupMetas)
    }

}