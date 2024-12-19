package com.masterplus.animals.core.shared_features.backup.data.repo

import com.masterplus.animals.core.shared_features.backup.data.mapper.toBackupMeta
import com.masterplus.animals.core.shared_features.backup.data.mapper.toBackupMetaEntity
import com.masterplus.animals.core.shared_features.backup.domain.models.BackupMeta
import com.masterplus.animals.core.shared_features.backup.domain.repo.BackupMetaRepo
import com.masterplus.animals.core.shared_features.database.dao.BackupMetaDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BackupMetaRepoImpl @Inject constructor(
    private val backupMetaDao: BackupMetaDao
): BackupMetaRepo {

    override suspend fun insertBackupMetas(backupMetas: List<BackupMeta>) {
        backupMetaDao.insertBackupMetas(backupMetas.map { it.toBackupMetaEntity() })
    }

    override suspend fun getLastBackupMeta(): BackupMeta? {
        return backupMetaDao.getLastBackupMeta()?.toBackupMeta()
    }

    override fun getBackupMetasFlow(): Flow<List<BackupMeta>> {
        return backupMetaDao.getBackupMetasFlow()
            .map { items-> items.map { it.toBackupMeta() } }
    }

    override suspend fun deleteBackupMetas() {
        backupMetaDao.deleteBackupMetas()
    }

    override suspend fun deleteBackupMetas(backupMetas: List<BackupMeta>) {
        backupMetaDao.deleteBackupMetas(backupMetas.map { it.toBackupMetaEntity() })
    }

    override suspend fun hasBackupMetas(): Boolean {
        return backupMetaDao.hasBackupMetas()
    }

    override suspend fun getExtraBackupMetas(offset: Int): List<BackupMeta> {
        return backupMetaDao.getExtraBackupMetas(offset).map { it.toBackupMeta() }
    }
}