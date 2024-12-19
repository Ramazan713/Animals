package com.masterplus.animals.core.shared_features.backup.data.repo

import com.google.firebase.storage.FirebaseStorage
import com.masterplus.animals.core.domain.constants.K
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.domain.utils.EmptyDefaultResult
import com.masterplus.animals.core.domain.utils.safeCall
import com.masterplus.animals.core.shared_features.backup.data.mapper.toBackupMeta
import com.masterplus.animals.core.shared_features.backup.domain.models.BackupMeta
import com.masterplus.animals.core.shared_features.backup.domain.repo.BackupMetaStorageService
import kotlinx.coroutines.tasks.await

class FirebaseBackupMetaStorageService: BackupMetaStorageService {

    private val rootRef = FirebaseStorage.getInstance().getReference("Backups/")

    override suspend fun getFiles(userId: String): DefaultResult<List<BackupMeta>> {
        return safeCall {
            val ref = rootRef.child(userId)
            val backupMetas = mutableListOf<BackupMeta>()

            val results = ref.listAll().await()
            results.items.forEach { item->
                val metadata = item.metadata.await()
                backupMetas.add(metadata.toBackupMeta())
            }
            return@safeCall backupMetas
        }
    }

    override suspend fun getFileData(userId: String, fileName: String): DefaultResult<ByteArray> {
        return safeCall {
            val ref = rootRef.child(userId).child(fileName)
            return@safeCall ref.getBytes(K.maxDownloadSizeBytes).await()
        }
    }

    override suspend fun deleteFile(userId: String, fileName: String): EmptyDefaultResult {
       return safeCall {
           val ref = rootRef.child(userId).child(fileName)
           ref.delete().await()
       }
    }

    override suspend fun uploadData(userId: String, fileName: String, data: ByteArray): EmptyDefaultResult {
        return safeCall {
            val ref = rootRef.child(userId).child(fileName)
            ref.putBytes(data).await()
        }
    }
}