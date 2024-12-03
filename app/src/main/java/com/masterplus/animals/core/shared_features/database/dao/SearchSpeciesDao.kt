package com.masterplus.animals.core.shared_features.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.masterplus.animals.core.shared_features.database.entity_helper.SpeciesDetailEmbedded

@Dao
interface SearchSpeciesDao {

    @Transaction
    @Query("""
        select * from species where
        (scientific_name like :query or name_tr like :query)
        order by case  when scientific_name like :queryOrder then 1 when name_tr like :queryOrder then 2 else 3 end
    """)
    fun searchPagingSpeciesTr(query: String, queryOrder: String): PagingSource<Int, SpeciesDetailEmbedded>

    @Transaction
    @Query("""
        select * from species where
        (scientific_name like :query or name_en like :query)
        order by case  when scientific_name like :queryOrder then 1 when name_en like :queryOrder then 2 else 3 end
    """)
    fun searchPagingSpeciesEn(query: String, queryOrder: String): PagingSource<Int, SpeciesDetailEmbedded>


    @Transaction
    @Query("""
         select S.* from species S, speciesRelationsView SR
        where SR.species_id = S.id and SR.order_id = :orderId and
        (S.scientific_name like :query or S.name_tr like :query)
        order by case  when scientific_name like :queryOrder then 1 when S.name_tr like :queryOrder then 2 else 3 end
    """)
    fun searchPagingSpeciesTrByOrderId(orderId: Int, query: String, queryOrder: String): PagingSource<Int, SpeciesDetailEmbedded>

    @Transaction
    @Query("""
         select S.* from species S, speciesRelationsView SR
        where SR.species_id = S.id and SR.order_id = :orderId and
        (S.scientific_name like :query or S.name_en like :query)
        order by case  when scientific_name like :queryOrder then 1 when S.name_en like :queryOrder then 2 else 3 end
    """)
    fun searchPagingSpeciesEnByOrderId(orderId: Int, query: String, queryOrder: String): PagingSource<Int, SpeciesDetailEmbedded>


    @Transaction
    @Query("""
        select S.* from species S, speciesRelationsView SR 
        where SR.species_id = S.id and SR.class_id = :classId and
        (S.scientific_name like :query or S.name_tr like :query)
        order by case  when scientific_name like :queryOrder then 1 when S.name_tr like :queryOrder then 2 else 3 end
    """)
    fun searchPagingSpeciesTrByClassId(classId: Int, query: String, queryOrder: String): PagingSource<Int, SpeciesDetailEmbedded>

    @Transaction
    @Query("""
        select S.* from species S, speciesRelationsView SR 
        where SR.species_id = S.id and SR.class_id = :classId and
        (S.scientific_name like :query or S.name_en like :query)
        order by case  when scientific_name like :queryOrder then 1 when S.name_en like :queryOrder then 2 else 3 end
    """)
    fun searchPagingSpeciesEnByClassId(classId: Int, query: String, queryOrder: String): PagingSource<Int, SpeciesDetailEmbedded>



    @Transaction
    @Query("""
        select S.* from species S, speciesRelationsView SR 
        where SR.species_id = S.id and SR.family_id = :familyId and
        (S.scientific_name like :query or S.name_tr like :query)
        order by case  when scientific_name like :queryOrder then 1 when S.name_tr like :queryOrder then 2 else 3 end
    """)
    fun searchPagingSpeciesTrByFamilyId(familyId: Int, query: String, queryOrder: String): PagingSource<Int, SpeciesDetailEmbedded>

    @Transaction
    @Query("""
        select S.* from species S, speciesRelationsView SR 
        where SR.species_id = S.id and SR.family_id = :familyId and
        (S.scientific_name like :query or S.name_en like :query)
        order by case  when scientific_name like :queryOrder then 1 when S.name_en like :queryOrder then 2 else 3 end
    """)
    fun searchPagingSpeciesEnByFamilyId(familyId: Int, query: String, queryOrder: String): PagingSource<Int, SpeciesDetailEmbedded>



    @Transaction
    @Query("""
        select * from species S, SpeciesHabitatCategories SHC where S.id = SHC.species_id and SHC.category_id = :habitatCategoryId and
        (scientific_name like :query or name_tr like :query)
        order by case  when scientific_name like :queryOrder then 1 when name_tr like :queryOrder then 2 else 3 end
    """)
    fun searchPagingSpeciesTrByHabitatCategoryId(habitatCategoryId: Int, query: String, queryOrder: String): PagingSource<Int, SpeciesDetailEmbedded>

    @Transaction
    @Query("""
        select * from species S, SpeciesHabitatCategories SHC where S.id = SHC.species_id and SHC.category_id = :habitatCategoryId and
        (scientific_name like :query or name_en like :query)
        order by case  when scientific_name like :queryOrder then 1 when name_en like :queryOrder then 2 else 3 end
    """)
    fun searchPagingSpeciesEnByHabitatCategoryId(habitatCategoryId: Int, query: String, queryOrder: String): PagingSource<Int, SpeciesDetailEmbedded>



    @Transaction
    @Query("""
        select S.* from species S, ListSpecies LA 
        where S.id = LA.speciesId and LA.listId = :listId and
        (S.scientific_name like :query or S.name_tr like :query)
        order by case  when scientific_name like :queryOrder then 1 when S.name_tr like :queryOrder then 2 else 3 end
    """)
    fun searchPagingSpeciesTrByListId(listId: Int, query: String, queryOrder: String): PagingSource<Int, SpeciesDetailEmbedded>

    @Transaction
    @Query("""
        select S.* from species S, ListSpecies LA 
        where S.id = LA.speciesId and LA.listId = :listId and
        (S.scientific_name like :query or S.name_en like :query)
        order by case  when scientific_name like :queryOrder then 1 when S.name_en like :queryOrder then 2 else 3 end
    """)
    fun searchPagingSpeciesEnByListId(listId: Int, query: String, queryOrder: String): PagingSource<Int, SpeciesDetailEmbedded>

}