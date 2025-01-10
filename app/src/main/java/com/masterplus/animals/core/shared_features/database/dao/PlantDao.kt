package com.masterplus.animals.core.shared_features.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.masterplus.animals.core.shared_features.database.entity.PlantEntity
import com.masterplus.animals.core.shared_features.database.entity_helper.PlantDetailEmbedded

@Dao
interface PlantDao {

    @Transaction
    @Query("""
        select * from species where id = :speciesId limit 1
    """)
    suspend fun getPlantDetailBySpeciesId(speciesId: Int): PlantDetailEmbedded?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlants(plants: List<PlantEntity>)
}