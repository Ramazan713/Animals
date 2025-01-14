package com.masterplus.animals.core.shared_features.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.masterplus.animals.core.shared_features.database.entity.SpeciesEntity
import com.masterplus.animals.core.shared_features.database.entity.SpeciesHabitatCategoryEntity
import com.masterplus.animals.core.shared_features.database.entity.SpeciesImageEntity
import com.masterplus.animals.core.shared_features.database.entity_helper.SpeciesDetailEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.SpeciesImageWithMetadataEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.SpeciesWithImagesEmbedded

@Dao
interface SpeciesDao {

    @Query("select * from species where id = :id limit 1")
    suspend fun getSpeciesById(id: Int): SpeciesEntity?

    @Query("select * from species where id = :id and label = :label limit 1")
    suspend fun getSpeciesByIdAndLabel(id: Int, label: String): SpeciesEntity?

    @Query("select * from species where id in (:ids) group by id")
    suspend fun getSpeciesByIds(ids: List<Int>): List<SpeciesWithImagesEmbedded>

    @Transaction
    @Query(
        """
        select * from species where label = :label order by order_key
    """
    )
    fun getPagingSpeciesByLabel(label: String): PagingSource<Int, SpeciesDetailEmbedded>

    @Transaction
    @Query(
        """
        select distinct S.* from species S, ListSpecies LA 
        where S.id = LA.speciesId and LA.listId = :listId group by S.id order by S.order_key
    """
    )
    fun getPagingSpeciesByListId(listId: Int): PagingSource<Int, SpeciesDetailEmbedded>


    @Query("""
        select count(distinct id) from species where kingdom_id = :kingdomId
    """)
    suspend fun getSpeciesCount(kingdomId: Int): Int

    @Query("""
        select * from species where kingdom_id = :kingdomId group by id order by order_key limit 1 offset :offset
    """)
    suspend fun getSpeciesByOffset(kingdomId: Int, offset: Int): SpeciesWithImagesEmbedded?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpecies(species: List<SpeciesEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpeciesHabitats(speciesHabitats: List<SpeciesHabitatCategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSpeciesImages(speciesImages: List<SpeciesImageEntity>)


    @Query("""
        SELECT DISTINCT category_id FROM specieshabitatcategories shc WHERE shc.species_id = :speciesId 
        AND NOT EXISTS (SELECT 1 FROM habitatcategories hc WHERE hc.id = shc.category_id)
    """)
    suspend fun getNotExistsHabitatIds(speciesId: Int): List<Int>

    @Query("""delete from species where label = :label""")
    suspend fun deleteSpeciesByLabel(label: String)

}