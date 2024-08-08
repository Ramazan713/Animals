package com.masterplus.animals.core.shared_features.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.masterplus.animals.core.shared_features.database.entity_helper.SpeciesDetailEmbedded

@Dao
interface SpeciesDao {

    @Transaction
    @Query("""
        select * from species order by id
    """)
    fun getPagingSpecies(): PagingSource<Int, SpeciesDetailEmbedded>

    @Transaction
    @Query("""
        select * from species where habitat_category_id = :habitatCategoryId order by id
    """)
    fun getPagingSpeciesByHabitatCategoryId(habitatCategoryId: Int): PagingSource<Int, SpeciesDetailEmbedded>

    @Transaction
    @Query("""
        select S.* from species S, speciesRelationsView SR 
        where SR.species_id = S.id and SR.class_id = :classId order by S.id
    """)
    fun getPagingSpeciesByClassId(classId: Int): PagingSource<Int, SpeciesDetailEmbedded>

    @Transaction
    @Query("""
        select S.* from species S, speciesRelationsView SR 
        where SR.species_id = S.id and SR.family_id = :familyId order by S.id
    """)
    fun getPagingSpeciesByFamilyId(familyId: Int): PagingSource<Int, SpeciesDetailEmbedded>

    @Transaction
    @Query("""
        select S.* from species S, speciesRelationsView SR
        where SR.species_id = S.id and SR.order_id = :orderId order by S.id
    """)
    fun getPagingSpeciesByOrderId(orderId: Int): PagingSource<Int, SpeciesDetailEmbedded>

    @Transaction
    @Query("""
        select S.* from species S, ListSpecies LA 
        where S.id = LA.speciesId and LA.listId = :listId order by S.id
    """)
    fun getPagingSpeciesByListId(listId: Int): PagingSource<Int, SpeciesDetailEmbedded>

}