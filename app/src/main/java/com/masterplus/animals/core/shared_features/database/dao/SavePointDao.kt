package com.masterplus.animals.core.shared_features.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.masterplus.animals.core.shared_features.database.entity.SavePointEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavePointDao {

    @Query("""
        select * from savepoints where id = :id
    """)
    suspend fun getSavePointById(id: Int): SavePointEntity?

    @Query("""
        select * from savepoints where destinationTypeId = :destinationTypeId and
        contentTypeId = :contentTypeId
    """)
    fun getFlowSavePointsDestinations(destinationTypeId: Int, contentTypeId: Int): Flow<List<SavePointEntity>>

    @Query("""
        select * from savepoints where destinationTypeId = :destinationTypeId and
        destinationId = :destinationId and contentTypeId = :contentTypeId
    """)
    fun getFlowSavePointsDestinationByDestId(destinationTypeId: Int, destinationId: Int, contentTypeId: Int): Flow<List<SavePointEntity>>


    @Query("""
        select * from savepoints where contentTypeId = :contentTypeId
    """)
    fun getAllFlowSavePointsByContentType(contentTypeId: Int): Flow<List<SavePointEntity>>

    @Query("""
        select * from savepoints where contentTypeId = :contentTypeId and destinationTypeId in (:destinationTypeIds)
    """)
    fun getAllFlowSavePointsByFilteredDestinations(contentTypeId: Int, destinationTypeIds: List<Int>): Flow<List<SavePointEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavePoint(savePoint: SavePointEntity)

    @Update
    suspend fun updateSavePoint(savePoint: SavePointEntity)

    @Query("""
        update savepoints set itemPosIndex = :itemPosIndex where id = :id
    """)
    suspend fun updateSavePointPos(id: Int, itemPosIndex: Int)

    @Query("""
        update savepoints set titleEn = :title, titleTr = :title where id = :id
    """)
    suspend fun updateSavePointTitle(id: Int, title: String)



    @Query("""
        delete from savepoints where id = :id
    """)
    suspend fun deleteSavePointById(id: Int)
}

