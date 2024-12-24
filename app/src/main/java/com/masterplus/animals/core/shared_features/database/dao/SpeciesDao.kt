package com.masterplus.animals.core.shared_features.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.masterplus.animals.core.shared_features.database.entity.ClassEntity
import com.masterplus.animals.core.shared_features.database.entity.FamilyEntity
import com.masterplus.animals.core.shared_features.database.entity.OrderEntity
import com.masterplus.animals.core.shared_features.database.entity.PhylumEntity
import com.masterplus.animals.core.shared_features.database.entity.SpeciesEntity
import com.masterplus.animals.core.shared_features.database.entity.SpeciesHabitatCategoryEntity
import com.masterplus.animals.core.shared_features.database.entity.SpeciesImageEntity
import com.masterplus.animals.core.shared_features.database.entity_helper.SpeciesDetailEmbedded

@Dao
interface SpeciesDao {

    @Query("select * from species where id = :id")
    suspend fun getSpeciesById(id: Int): SpeciesEntity?


    @Transaction
    @Query("""
        select * from species where kingdom_id = :kingdomId order by id
    """)
    fun getPagingSpecies(kingdomId: Int): PagingSource<Int, SpeciesDetailEmbedded>

    @Transaction
    @Query("""
        select * from species where label = :label order by id
    """)
    fun getPagingSpeciesByLabel(label: String): PagingSource<Int, SpeciesDetailEmbedded>

    @Transaction
    @Query("""
        select S.* from species S, SpeciesHabitatCategories SHC
        where S.id = SHC.species_id and SHC.category_id = :habitatCategoryId and S.kingdom_id = :kingdomId order by id
    """)
    fun getPagingSpeciesByHabitatCategoryId(habitatCategoryId: Int, kingdomId: Int): PagingSource<Int, SpeciesDetailEmbedded>

    @Transaction
    @Query("""
        select * from species where class_id = :classId and kingdom_id = :kingdomId order by id
    """)
    fun getPagingSpeciesByClassId(classId: Int, kingdomId: Int): PagingSource<Int, SpeciesDetailEmbedded>

    @Transaction
    @Query("""
        select * from species where family_id = :familyId and kingdom_id = :kingdomId order by id
    """)
    fun getPagingSpeciesByFamilyId(familyId: Int, kingdomId: Int): PagingSource<Int, SpeciesDetailEmbedded>

    @Transaction
    @Query("""
        select * from species where order_id = :orderId and kingdom_id = :kingdomId order by id
    """)
    fun getPagingSpeciesByOrderId(orderId: Int, kingdomId: Int): PagingSource<Int, SpeciesDetailEmbedded>

    @Transaction
    @Query("""
        select S.* from species S, ListSpecies LA 
        where S.id = LA.speciesId and LA.listId = :listId order by S.id
    """)
    fun getPagingSpeciesByListId(listId: Int): PagingSource<Int, SpeciesDetailEmbedded>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpecies(species: List<SpeciesEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpeciesHabitats(speciesHabitats: List<SpeciesHabitatCategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpeciesImages(speciesImages: List<SpeciesImageEntity>)


    @Query("""
        select distinct category_id from specieshabitatcategories where species_id = :speciesId and 
        category_id not in (select distinct id from habitatcategories)
    """)
    suspend fun getNotExistsHabitatIds(speciesId: Int): List<Int>

}