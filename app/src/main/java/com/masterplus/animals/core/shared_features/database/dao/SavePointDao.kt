package com.masterplus.animals.core.shared_features.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.masterplus.animals.core.shared_features.database.entity.SavePointEntity
import com.masterplus.animals.core.shared_features.database.entity_helper.SavePointWithImageEmbedded
import kotlinx.coroutines.flow.Flow

@Dao
interface SavePointDao {

    @Query("""
        select * from savepoints where id = :id
    """)
    suspend fun getSavePointById(id: Int): SavePointWithImageEmbedded?

    @Query("""
        select * from savepoints where contentTypeId = :contentTypeId and kingdomId = :kingdomId and
        destinationTypeId = :destinationTypeId and saveModeId = :saveModeId
        order by id desc limit 1
    """)
    suspend fun getSavePointByQuery(
        destinationTypeId: Int, contentTypeId: Int, kingdomId: Int,
        saveModeId: Int
    ): SavePointWithImageEmbedded?

    @Query("""
        select * from savepoints where contentTypeId = :contentTypeId and kingdomId = :kingdomId and
        destinationTypeId = :destinationTypeId and saveModeId = :saveModeId and destinationId = :destinationId
        order by id desc limit 1
    """)
    suspend fun getSavePointByQuery(
        destinationTypeId: Int, contentTypeId: Int, kingdomId: Int,
        saveModeId: Int, destinationId: Int
    ): SavePointWithImageEmbedded?



    @Query("""
        select * from savepoints where destinationTypeId = :destinationTypeId and
        contentTypeId = :contentTypeId and kingdomId = :kingdomId order by modifiedTime desc
    """)
    fun getFlowSavePointsDestinations(
        destinationTypeId: Int,
        contentTypeId: Int,
        kingdomId: Int
    ): Flow<List<SavePointWithImageEmbedded>>

    @Query("""
        select * from savepoints where destinationTypeId = :destinationTypeId and
        contentTypeId = :contentTypeId and kingdomId = :kingdomId and saveModeId = :saveModeId order by modifiedTime desc
    """)
    fun getFlowSavePointsDestinations(
        destinationTypeId: Int,
        contentTypeId: Int,
        kingdomId: Int,
        saveModeId: Int
    ): Flow<List<SavePointWithImageEmbedded>>

    @Query("""
        select * from savepoints where destinationTypeId = :destinationTypeId and
        destinationId = :destinationId and contentTypeId = :contentTypeId and kingdomId = :kingdomId
        order by modifiedTime desc
    """)
    fun getFlowSavePointsDestinationByDestId(
        destinationTypeId: Int,
        destinationId: Int,
        contentTypeId: Int,
        kingdomId: Int
    ): Flow<List<SavePointWithImageEmbedded>>

    @Query("""
        select * from savepoints where destinationTypeId = :destinationTypeId and
        destinationId = :destinationId and contentTypeId = :contentTypeId and kingdomId = :kingdomId and saveModeId = :saveModeId
        order by modifiedTime desc
    """)
    fun getFlowSavePointsDestinationByDestId(
        destinationTypeId: Int,
        destinationId: Int,
        contentTypeId: Int,
        kingdomId: Int,
        saveModeId: Int
    ): Flow<List<SavePointWithImageEmbedded>>





    @Query("""
        select * from savepoints where contentTypeId = :contentTypeId and kingdomId = :kingdomId 
        order by modifiedTime desc
    """)
    fun getAllFlowSavePointsByContentType(contentTypeId: Int, kingdomId: Int): Flow<List<SavePointWithImageEmbedded>>

    @Query("""
        select * from savepoints where contentTypeId = :contentTypeId and kingdomId = :kingdomId 
        and saveModeId = :saveModeId
        order by modifiedTime desc
    """)
    fun getAllFlowSavePointsByContentType(contentTypeId: Int, kingdomId: Int, saveModeId: Int): Flow<List<SavePointWithImageEmbedded>>

    @Query("""
        select * from savepoints where contentTypeId = :contentTypeId and destinationTypeId in (:destinationTypeIds)
        and kingdomId = :kingdomId
        order by modifiedTime desc
    """)
    fun getAllFlowSavePointsByFilteredDestinations(
        contentTypeId: Int,
        destinationTypeIds: List<Int>,
        kingdomId: Int
    ): Flow<List<SavePointWithImageEmbedded>>

    @Query("""
        select * from savepoints where contentTypeId = :contentTypeId and destinationTypeId in (:destinationTypeIds)
        and kingdomId = :kingdomId and saveModeId = :saveModeId
        order by modifiedTime desc
    """)
    fun getAllFlowSavePointsByFilteredDestinations(
        contentTypeId: Int,
        destinationTypeIds: List<Int>,
        kingdomId: Int,
        saveModeId: Int
    ): Flow<List<SavePointWithImageEmbedded>>




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

