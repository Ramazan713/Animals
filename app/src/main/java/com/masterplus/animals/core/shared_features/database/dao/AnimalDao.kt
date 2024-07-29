package com.masterplus.animals.core.shared_features.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.masterplus.animals.core.shared_features.database.entity_helper.AnimalDataWithImagesEntity
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
        select * from animals limit 1
    """)
    suspend fun getAnimals(): List<AnimalDataWithImagesEntity>


    @Transaction
    @Query("""
        select * from animals order by id
    """)
    fun getPagingAnimals(): PagingSource<Int, AnimalDataWithImagesEntity>

    @Transaction
    @Query("""
        select * from animals where habitat_category_id = :habitatCategoryId order by id
    """)
    fun getPagingAnimalsByHabitatCategoryId(habitatCategoryId: Int): PagingSource<Int, AnimalDataWithImagesEntity>

    @Transaction
    @Query("""
        select * from animals where class_id = :classId order by id
    """)
    fun getPagingAnimalsByClassId(classId: Int): PagingSource<Int, AnimalDataWithImagesEntity>

    @Transaction
    @Query("""
        select * from animals where family_id = :familyId order by id
    """)
    fun getPagingAnimalsByFamilyId(familyId: Int): PagingSource<Int, AnimalDataWithImagesEntity>

    @Transaction
    @Query("""
        select * from animals where order_id = :orderId order by id
    """)
    fun getPagingAnimalsByOrderId(orderId: Int): PagingSource<Int, AnimalDataWithImagesEntity>

}