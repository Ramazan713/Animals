package com.masterplus.animals.core.shared_features.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.masterplus.animals.core.shared_features.database.entity_helper.AnimalDetailEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.AnimalDataEmbedded

@Dao
interface AnimalDao {

    @Transaction
    @Query("""
        select S.* from species S, Animals A 
        where S.id = A.species_id and A.id = :id
    """)
    suspend fun getAnimalById(id: Int): AnimalDataEmbedded?

    @Transaction
    @Query("""
        select S.* from species S, Animals A  
        where S.id = A.species_id and A.id = :id
    """)
    suspend fun getAnimalDetailById(id: Int): AnimalDetailEmbedded?

    @Query("""
        select count(*) from animals
    """)
    suspend fun getAnimalsSize(): Int

    @Transaction
    @Query("""
        select S.* from species S, Animals A 
        where S.id = A.species_id limit 1 offset :offset
    """)
    suspend fun getAnimalByOffset(offset: Int): AnimalDataEmbedded?

}