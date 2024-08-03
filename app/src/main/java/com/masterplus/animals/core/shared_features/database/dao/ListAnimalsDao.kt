package com.masterplus.animals.core.shared_features.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.masterplus.animals.core.shared_features.database.entity.ListAnimalsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ListAnimalsDao {

    @Query("""
        select * from listAnimals where 
        animalId=:animalId and listId=:listId
    """)
    suspend fun getListAnimalsEntity(animalId: Int, listId: Int): ListAnimalsEntity?


    @Query("""
        select * from listAnimals where listId=:listId
    """)
    suspend fun getListAnimalsByListId(listId: Int): List<ListAnimalsEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListAnimal(listAnimal: ListAnimalsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListAnimals(listAnimals: List<ListAnimalsEntity>)

    @Delete
    suspend fun deleteListAnimal(listAnimals: ListAnimalsEntity)



    @Transaction
    @Query("""
        select exists(select * from listAnimals LA, lists L
        where LA.listId = L.id and L.isRemovable = 0 and LA.animalId = :animalId)
    """)
    fun hasAnimalInFavoriteListFlow(animalId: Int): Flow<Boolean>

    @Transaction
    @Query("""
        select exists(select * from listAnimals LA, lists L
        where LA.listId = L.id and L.isRemovable = 1 and LA.animalId = :animalId)
    """)
    fun hasAnimalInRemovableListFlow(animalId: Int): Flow<Boolean>
}