package com.masterplus.animals.core.shared_features.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.masterplus.animals.core.shared_features.database.entity_helper.AnimalDataDetailEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.AnimalDetailEmbeddedEntity
import com.masterplus.animals.core.shared_features.database.entity_helper.AnimalWithImagesEntity

@Dao
interface AnimalDao {

    @Transaction
    @Query("""
        select * from animals where id = :id
    """)
    suspend fun getAnimalById(id: Int): AnimalWithImagesEntity?

    @Transaction
    @Query("""
        select * from animals where id = :id
    """)
    suspend fun getAnimalDetailById(id: Int): AnimalDetailEmbeddedEntity?

    @Query("""
        select count(*) from animals
    """)
    suspend fun getAnimalsSize(): Int

    @Transaction
    @Query("""
        select * from animals limit 1 offset :offset
    """)
    suspend fun getAnimalByOffset(offset: Int): AnimalDataDetailEmbedded?


    @Transaction
    @Query("""
        select * from animals order by id
    """)
    fun getPagingAnimals(): PagingSource<Int, AnimalDataDetailEmbedded>

    @Transaction
    @Query("""
        select * from animals where habitat_category_id = :habitatCategoryId order by id
    """)
    fun getPagingAnimalsByHabitatCategoryId(habitatCategoryId: Int): PagingSource<Int, AnimalDataDetailEmbedded>

    @Transaction
    @Query("""
        select * from animals where class_id = :classId order by id
    """)
    fun getPagingAnimalsByClassId(classId: Int): PagingSource<Int, AnimalDataDetailEmbedded>

    @Transaction
    @Query("""
        select * from animals where family_id = :familyId order by id
    """)
    fun getPagingAnimalsByFamilyId(familyId: Int): PagingSource<Int, AnimalDataDetailEmbedded>

    @Transaction
    @Query("""
        select * from animals where order_id = :orderId order by id
    """)
    fun getPagingAnimalsByOrderId(orderId: Int): PagingSource<Int, AnimalDataDetailEmbedded>

    @Transaction
    @Query("""
        select A.* from animals A, ListAnimals LA 
        where A.id = LA.animalId and LA.listId = :listId order by A.id
    """)
    fun getPagingAnimalsByListId(listId: Int): PagingSource<Int, AnimalDataDetailEmbedded>

}