package com.masterplus.animals.core.shared_features.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.masterplus.animals.core.shared_features.database.entity.SpeciesEntity
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
        select S.* from species S, SpeciesHabitatCategories SHC
        where S.id = SHC.species_id and SHC.category_id = :habitatCategoryId and S.kingdom_id = :kingdomId order by id
    """)
    fun getPagingSpeciesByHabitatCategoryId(habitatCategoryId: Int, kingdomId: Int): PagingSource<Int, SpeciesDetailEmbedded>

    @Transaction
    @Query("""
        select S.* from species S, speciesRelationsView SR 
        where SR.species_id = S.id and SR.class_id = :classId and S.kingdom_id = :kingdomId order by S.id
    """)
    fun getPagingSpeciesByClassId(classId: Int, kingdomId: Int): PagingSource<Int, SpeciesDetailEmbedded>

    @Transaction
    @Query("""
        select S.* from species S, speciesRelationsView SR 
        where SR.species_id = S.id and SR.family_id = :familyId and S.kingdom_id = :kingdomId order by S.id
    """)
    fun getPagingSpeciesByFamilyId(familyId: Int, kingdomId: Int): PagingSource<Int, SpeciesDetailEmbedded>

    @Transaction
    @Query("""
        select S.* from species S, speciesRelationsView SR
        where SR.species_id = S.id and SR.order_id = :orderId and S.kingdom_id = :kingdomId order by S.id
    """)
    fun getPagingSpeciesByOrderId(orderId: Int, kingdomId: Int): PagingSource<Int, SpeciesDetailEmbedded>

    @Transaction
    @Query("""
        select S.* from species S, ListSpecies LA 
        where S.id = LA.speciesId and LA.listId = :listId order by S.id
    """)
    fun getPagingSpeciesByListId(listId: Int): PagingSource<Int, SpeciesDetailEmbedded>

}