package com.masterplus.animals.core.shared_features.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.masterplus.animals.core.shared_features.database.entity.ListSpeciesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ListSpeciesDao {

    @Query("""
        select * from listSpecies where 
        speciesId=:speciesId and listId = :listId
    """)
    suspend fun getListSpecies(speciesId: Int, listId: Int): ListSpeciesEntity?


    @Query("""
        select * from listSpecies where listId = :listId
    """)
    suspend fun getListSpeciesByListId(listId: Int): List<ListSpeciesEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListSpecies(listSpecies: ListSpeciesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListSpecies(listSpecies: List<ListSpeciesEntity>)

    @Delete
    suspend fun deleteListSpecies(listSpecies: ListSpeciesEntity)



    @Transaction
    @Query("""
        select exists(select * from listSpecies LA, lists L
        where LA.listId = L.id and L.isRemovable = 0 and LA.speciesId = :speciesId)
    """)
    fun hasSpeciesInFavoriteListFlow(speciesId: Int): Flow<Boolean>

    @Transaction
    @Query("""
        select exists(select * from listSpecies LA, lists L
        where LA.listId = L.id and L.isRemovable = 1 and LA.speciesId = :speciesId)
    """)
    fun hasSpeciesInRemovableListFlow(speciesId: Int): Flow<Boolean>
}