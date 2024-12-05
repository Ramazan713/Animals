package com.masterplus.animals.core.shared_features.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.masterplus.animals.core.shared_features.database.entity_helper.PlantDataEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.PlantDetailEmbedded

@Dao
interface PlantDao {

    @Transaction
    @Query("""
        select S.* from species S, Plants P 
        where S.id = P.species_id and P.id = :id
    """)
    suspend fun getPlantById(id: Int): PlantDataEmbedded?

    @Transaction
    @Query("""
        select S.* from species S, Plants P  
        where S.id = P.species_id and P.id = :id
    """)
    suspend fun getPlantDetailById(id: Int): PlantDetailEmbedded?

    @Transaction
    @Query("""
        select * from species where id = :speciesId
    """)
    suspend fun getPlantDetailBySpeciesId(speciesId: Int): PlantDetailEmbedded?

    @Query("""
        select count(*) from animals
    """)
    suspend fun getPlantsSize(): Int

    @Transaction
    @Query("""
        select S.* from species S, Plants P 
        where S.id = P.species_id limit 1 offset :offset
    """)
    suspend fun getPlantByOffset(offset: Int): PlantDataEmbedded?
}