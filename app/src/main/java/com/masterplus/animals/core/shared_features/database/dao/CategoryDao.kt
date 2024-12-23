package com.masterplus.animals.core.shared_features.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.masterplus.animals.core.shared_features.database.entity.ClassEntity
import com.masterplus.animals.core.shared_features.database.entity.FamilyEntity
import com.masterplus.animals.core.shared_features.database.entity.HabitatCategoryEntity
import com.masterplus.animals.core.shared_features.database.entity.ImageEntity
import com.masterplus.animals.core.shared_features.database.entity.ImageMetadataEntity
import com.masterplus.animals.core.shared_features.database.entity.OrderEntity
import com.masterplus.animals.core.shared_features.database.entity.PhylumEntity
import com.masterplus.animals.core.shared_features.database.entity_helper.ClassWithImageEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.FamilyWithImageEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.HabitatWithImageEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.OrderWithImageEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.PhylumWithImageEmbedded

@Dao
interface CategoryDao {
    @Transaction
    @Query("""
        select * from classes where id = :classId
    """)
    suspend fun getClassWithId(classId: Int): ClassWithImageEmbedded?

    @Transaction
    @Query("""
        select * from orders where id = :orderId
    """)
    suspend fun getOrderWithId(orderId: Int): OrderWithImageEmbedded?

    @Transaction
    @Query("""
        select * from families where id = :familyId
    """)
    suspend fun getFamilyWithId(familyId: Int): FamilyWithImageEmbedded?

    @Transaction
    @Query("""
        select * from habitatcategories where id = :habitatCategoryId
    """)
    suspend fun getHabitatCategoryWithId(habitatCategoryId: Int): HabitatWithImageEmbedded?


    @Transaction
    @Query("""
        select * from classes where kingdom_id = :kingdomId
        order by id asc limit :limit
    """)
    suspend fun getClasses(limit: Int, kingdomId: Int): List<ClassWithImageEmbedded>

    @Transaction
    @Query("""
        select * from classes where kingdom_id = :kingdomId order by id asc
    """)
    fun getPagingClasses(kingdomId: Int): PagingSource<Int, ClassWithImageEmbedded>


    @Transaction
    @Query("""
        select * from families where kingdom_id = :kingdomId
        order by id asc limit :limit
    """)
    suspend fun getFamilies(limit: Int, kingdomId: Int): List<FamilyWithImageEmbedded>

    @Transaction
    @Query("""
        select * from families where kingdom_id = :kingdomId and order_id = :orderId order by id asc
    """)
    fun getPagingFamiliesWithOrderId(orderId: Int, kingdomId: Int): PagingSource<Int, FamilyWithImageEmbedded>

    @Transaction
    @Query("""
        select * from families where kingdom_id = :kingdomId
        order by id asc
    """)
    fun getPagingFamilies(kingdomId: Int): PagingSource<Int, FamilyWithImageEmbedded>


    @Transaction
    @Query("""
        select * from habitatcategories order by id asc limit :limit
    """)
    suspend fun getHabitatCategories(limit: Int): List<HabitatWithImageEmbedded>


    @Transaction
    @Query("""
        select * from orders where kingdom_id = :kingdomId
        order by id asc limit :limit
    """)
    suspend fun getOrders(limit: Int, kingdomId: Int): List<OrderWithImageEmbedded>

    @Transaction
    @Query("""
        select * from orders where kingdom_id = :kingdomId and class_id = :classId order by id asc
    """)
    fun getPagingOrdersWithClassId(classId: Int, kingdomId: Int): PagingSource<Int, OrderWithImageEmbedded>

    @Transaction
    @Query("""
        select * from orders where kingdom_id = :kingdomId 
        order by id asc
    """)
    fun getPagingOrders(kingdomId: Int): PagingSource<Int, OrderWithImageEmbedded>

    @Transaction
    @Query("""
        select * from habitatkingdomview where kingdom_id = :kingdomId order by id asc
    """)
    fun getPagingHabitats(kingdomId: Int): PagingSource<Int, HabitatWithImageEmbedded>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhylums(phylums: List<PhylumEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClasses(classEntities: List<ClassEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrders(orders: List<OrderEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFamilies(classEntities: List<FamilyEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabitats(habitats: List<HabitatCategoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(images: List<ImageEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMetadatas(metadatas: List<ImageMetadataEntity>)


    @Transaction
    suspend fun insertPhylumWithImages(phylumWithImages: List<PhylumWithImageEmbedded>){
        val imageEntities = phylumWithImages.mapNotNull { it.image?.image }
        val imageMetadataEntities = phylumWithImages.mapNotNull { it.image?.metadata }
        val phylumEntities = phylumWithImages.map { it.phylum }
        insertImages(imageEntities)
        insertMetadatas(imageMetadataEntities)
        insertPhylums(phylumEntities)
    }

    @Transaction
    suspend fun insertClassesWithImages(classWithImages: List<ClassWithImageEmbedded>){
        val imageEntities = classWithImages.mapNotNull { it.image?.image }
        val imageMetadataEntities = classWithImages.mapNotNull { it.image?.metadata }
        val classEntities = classWithImages.map { it.classEntity }
        insertImages(imageEntities)
        insertMetadatas(imageMetadataEntities)
        insertClasses(classEntities)
    }

    @Transaction
    suspend fun insertOrdersWithImages(orderWithImages: List<OrderWithImageEmbedded>){
        val imageEntities = orderWithImages.mapNotNull { it.image?.image }
        val imageMetadataEntities = orderWithImages.mapNotNull { it.image?.metadata }
        val orderEntities = orderWithImages.map { it.order }
        insertImages(imageEntities)
        insertMetadatas(imageMetadataEntities)
        insertOrders(orderEntities)
    }

    @Transaction
    suspend fun insertFamiliesWithImages(familiesWithImages: List<FamilyWithImageEmbedded>){
        val imageEntities = familiesWithImages.mapNotNull { it.image?.image }
        val imageMetadataEntities = familiesWithImages.mapNotNull { it.image?.metadata }
        val familyEntities = familiesWithImages.map { it.family }
        insertImages(imageEntities)
        insertMetadatas(imageMetadataEntities)
        insertFamilies(familyEntities)
    }

    @Transaction
    suspend fun insertHabitatsWithImages(habitatWithImages: List<HabitatWithImageEmbedded>){
        val imageEntities = habitatWithImages.mapNotNull { it.image?.image }
        val imageMetadataEntities = habitatWithImages.mapNotNull { it.image?.metadata }
        val habitatEntities = habitatWithImages.map { it.habitat }
        insertImages(imageEntities)
        insertMetadatas(imageMetadataEntities)
        insertHabitats(habitatEntities)
    }


    @Query("delete from phylums where kingdom_id = :kingdomId")
    suspend fun deletePhylums(kingdomId: Int)

    @Query("delete from classes where kingdom_id = :kingdomId")
    suspend fun deleteClasses(kingdomId: Int)

    @Query("delete from classes where phylum_id = :phylumId and kingdom_id = :kingdomId")
    suspend fun deleteClasses(phylumId: Int, kingdomId: Int)


    @Query("delete from orders where kingdom_id = :kingdomId")
    suspend fun deleteOrders(kingdomId: Int)

    @Query("delete from orders where class_id = :classId and kingdom_id = :kingdomId")
    suspend fun deleteOrders(classId: Int, kingdomId: Int)


    @Query("delete from families where kingdom_id = :kingdomId")
    suspend fun deleteFamilies(kingdomId: Int)

    @Query("delete from families where order_id = :orderId and kingdom_id = :kingdomId")
    suspend fun deleteFamilies(orderId: Int, kingdomId: Int)


    @Query("delete from habitatcategories where id in (select id from habitatkingdomview where kingdom_id = :kingdomId)")
    suspend fun deleteHabitats(kingdomId: Int)
}