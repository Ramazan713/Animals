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
        group by id
        order by case when scientific_name like :queryOrder then 1 when name_tr like :queryOrder then 2 else 3 end
    """)
    fun searchPagingSpeciesTr(query: String, queryOrder: String): PagingSource<Int, SpeciesDetailEmbedded>

    @Transaction
    @Query("""
        select * from species where
        (scientific_name like :query or name_en like :query)
        group by id
        order by case  when scientific_name like :queryOrder then 1 when name_en like :queryOrder then 2 else 3 end
    """)
    fun searchPagingSpeciesEn(query: String, queryOrder: String): PagingSource<Int, SpeciesDetailEmbedded>


    @Transaction
    @Query("""
         select * from species where order_id = :orderId and (scientific_name like :query or name_tr like :query)
         group by id
        order by case  when scientific_name like :queryOrder then 1 when name_tr like :queryOrder then 2 else 3 end
    """)
    fun searchPagingSpeciesTrByOrderId(orderId: Int, query: String, queryOrder: String): PagingSource<Int, SpeciesDetailEmbedded>

    @Transaction
    @Query("""
         select * from species where order_id = :orderId and
        (scientific_name like :query or name_en like :query) group by id
        order by case  when scientific_name like :queryOrder then 1 when name_en like :queryOrder then 2 else 3 end
    """)
    fun searchPagingSpeciesEnByOrderId(orderId: Int, query: String, queryOrder: String): PagingSource<Int, SpeciesDetailEmbedded>


    @Transaction
    @Query("""
        select * from species where class_id = :classId and
        (scientific_name like :query or name_tr like :query) group by id
        order by case  when scientific_name like :queryOrder then 1 when name_tr like :queryOrder then 2 else 3 end
    """)
    fun searchPagingSpeciesTrByClassId(classId: Int, query: String, queryOrder: String): PagingSource<Int, SpeciesDetailEmbedded>

    @Transaction
    @Query("""
        select * from species where class_id = :classId and
        (scientific_name like :query or name_en like :query) group by id
        order by case  when scientific_name like :queryOrder then 1 when name_en like :queryOrder then 2 else 3 end
    """)
    fun searchPagingSpeciesEnByClassId(classId: Int, query: String, queryOrder: String): PagingSource<Int, SpeciesDetailEmbedded>



    @Transaction
    @Query("""
        select * from species where family_id = :familyId and
        (scientific_name like :query or name_tr like :query) group by id
        order by case  when scientific_name like :queryOrder then 1 when name_tr like :queryOrder then 2 else 3 end
    """)
    fun searchPagingSpeciesTrByFamilyId(familyId: Int, query: String, queryOrder: String): PagingSource<Int, SpeciesDetailEmbedded>

    @Transaction
    @Query("""
        select * from species where family_id = :familyId and
        (scientific_name like :query or name_en like :query) group by id
        order by case  when scientific_name like :queryOrder then 1 when name_en like :queryOrder then 2 else 3 end
    """)
    fun searchPagingSpeciesEnByFamilyId(familyId: Int, query: String, queryOrder: String): PagingSource<Int, SpeciesDetailEmbedded>



    @Transaction
    @Query("""
        select S.* from species S, SpeciesHabitatCategories SHC, HabitatKingdoms HK
        where S.id = SHC.species_id and HK.habitat_id = SHC.category_id and SHC.category_id = :habitatCategoryId and
        S.kingdom_id = :kingdomId and S.kingdom_id = HK.kingdom_id and
        (scientific_name like :query or name_tr like :query) group by S.id
        order by case  when scientific_name like :queryOrder then 1 when name_tr like :queryOrder then 2 else 3 end
    """)
    fun searchPagingSpeciesTrByHabitatCategoryId(
        habitatCategoryId: Int,
        kingdomId: Int,
        query: String,
        queryOrder: String
    ): PagingSource<Int, SpeciesDetailEmbedded>

    @Transaction
    @Query("""
        select S.* from species S, SpeciesHabitatCategories SHC, HabitatKingdoms HK
        where S.id = SHC.species_id and HK.habitat_id = SHC.category_id and SHC.category_id = :habitatCategoryId and
        S.kingdom_id = :kingdomId and S.kingdom_id = HK.kingdom_id and
        (scientific_name like :query or name_en like :query) group by S.id
        order by case  when scientific_name like :queryOrder then 1 when name_en like :queryOrder then 2 else 3 end
    """)
    fun searchPagingSpeciesEnByHabitatCategoryId(
        habitatCategoryId: Int,
        kingdomId: Int,
        query: String,
        queryOrder: String
    ): PagingSource<Int, SpeciesDetailEmbedded>



    @Transaction
    @Query("""
        select S.* from species S, ListSpecies LA 
        where S.id = LA.speciesId and LA.listId = :listId and
        (S.scientific_name like :query or S.name_tr like :query) group by S.id
        order by case  when scientific_name like :queryOrder then 1 when S.name_tr like :queryOrder then 2 else 3 end
    """)
    fun searchPagingSpeciesTrByListId(listId: Int, query: String, queryOrder: String): PagingSource<Int, SpeciesDetailEmbedded>

    @Transaction
    @Query("""
        select S.* from species S, ListSpecies LA 
        where S.id = LA.speciesId and LA.listId = :listId and
        (S.scientific_name like :query or S.name_en like :query) group by S.id
        order by case  when scientific_name like :queryOrder then 1 when S.name_en like :queryOrder then 2 else 3 end
    """)
    fun searchPagingSpeciesEnByListId(listId: Int, query: String, queryOrder: String): PagingSource<Int, SpeciesDetailEmbedded>

}