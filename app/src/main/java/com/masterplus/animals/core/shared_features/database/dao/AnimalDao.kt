package com.masterplus.animals.core.shared_features.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.masterplus.animals.core.shared_features.database.entity.AnimalEntity
import com.masterplus.animals.core.shared_features.database.entity_helper.AnimalDetailEmbedded

@Dao
interface AnimalDao {

    @Transaction
    @Query("""
        select * from species where id = :speciesId limit 1
    """)
    suspend fun getAnimalDetailBySpeciesId(speciesId: Int): AnimalDetailEmbedded?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimals(animals: List<AnimalEntity>)
}