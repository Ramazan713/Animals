package com.masterplus.animals.core.shared_features.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.masterplus.animals.core.shared_features.database.entity.BackupMetaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BackupMetaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBackupMeta(backupMetaEntity: BackupMetaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBackupMetas(backupMetaEntities: List<BackupMetaEntity>)

    @Query("""select * from backupMetas order by updatedDate desc limit 1""")
    suspend fun getLastBackupMeta(): BackupMetaEntity?

    @Query("""select * from backupMetas order by updatedDate desc""")
    fun getBackupMetasFlow(): Flow<List<BackupMetaEntity>>

    @Query("""delete from backupMetas""")
    suspend fun deleteBackupMetas()

    @Query("""select * from backupMetas order by updatedDate desc limit 10 offset :offset""")
    suspend fun getExtraBackupMetas(offset: Int): List<BackupMetaEntity>

    @Query("""select exists(select * from backupMetas)""")
    suspend fun hasBackupMetas(): Boolean

    @Delete
    suspend fun deleteBackupMetas(backupMetaEntities: List<BackupMetaEntity>)
}