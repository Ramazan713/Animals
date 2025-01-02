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

    @Query("""
        select * from phylums where id = :phylumId limit 1
    """)
    suspend fun getPhylumWithId2(phylumId: Int): PhylumEntity?

    @Query("""
        select * from phylums where id = :phylumId and label = :label limit 1
    """)
    suspend fun getPhylumWithId2(phylumId: Int, label: String): PhylumEntity?

    @Query("SELECT COUNT(*) FROM phylums WHERE id < :itemId and label = :label")
    suspend fun getPhylumPosByLabel(itemId: Int, label: String): Int?



    @Transaction
    @Query("""
        select * from classes where id = :classId limit 1
    """)
    suspend fun getClassWithId(classId: Int): ClassWithImageEmbedded?

    @Query("""
        select * from classes where id = :classId limit 1
    """)
    suspend fun getClassWithId2(classId: Int): ClassEntity?

    @Query("""
        select * from classes where id = :classId and label = :label limit 1
    """)
    suspend fun getClassWithId2(classId: Int, label: String): ClassEntity?

    @Query("SELECT COUNT(*) FROM classes WHERE id < :itemId and label = :label")
    suspend fun getClassPosByLabel(itemId: Int, label: String): Int?


    @Transaction
    @Query("""
        select * from orders where id = :orderId limit 1
    """)
    suspend fun getOrderWithId(orderId: Int): OrderWithImageEmbedded?

    @Query("""
        select * from orders where id = :orderId limit 1
    """)
    suspend fun getOrderWithId2(orderId: Int): OrderEntity?

    @Query("""
        select * from orders where id = :orderId and label = :label limit 1
    """)
    suspend fun getOrderWithId2(orderId: Int, label: String): OrderEntity?

    @Query("SELECT COUNT(*) FROM orders WHERE id < :itemId and label = :label")
    suspend fun getOrderPosByLabel(itemId: Int, label: String): Int?

    @Transaction
    @Query("""
        select * from families where id = :familyId limit 1
    """)
    suspend fun getFamilyWithId(familyId: Int): FamilyWithImageEmbedded?

    @Query("""
        select * from families where id = :familyId limit 1
    """)
    suspend fun getFamilyWithId2(familyId: Int): FamilyEntity?

    @Query("""
        select * from families where id = :familyId and label = :label limit 1
    """)
    suspend fun getFamilyWithId2(familyId: Int, label: String): FamilyEntity?

    @Query("SELECT COUNT(*) FROM families WHERE id < :itemId and label = :label")
    suspend fun getFamilyPosByLabel(itemId: Int, label: String): Int?


    @Transaction
    @Query("""
        select * from habitatcategories where id = :habitatCategoryId limit 1
    """)
    suspend fun getHabitatCategoryWithId(habitatCategoryId: Int): HabitatWithImageEmbedded?

    @Query("""
        select * from habitatcategories where id = :habitatCategoryId limit 1
    """)
    suspend fun getHabitatCategoryWithId2(habitatCategoryId: Int): HabitatCategoryEntity?

    @Query("""
        select * from habitatcategories where id = :habitatCategoryId and label = :label limit 1
    """)
    suspend fun getHabitatCategoryWithId2(habitatCategoryId: Int, label: String): HabitatCategoryEntity?

    @Query("SELECT COUNT(*) FROM habitatcategories WHERE id < :itemId and label = :label")
    suspend fun getHabitatPosByLabel(itemId: Int, label: String): Int?


    @Transaction
    @Query(
        """
        select * from classes where label = :label
        order by order_key asc limit :limit
    """
    )
    suspend fun getClasses(limit: Int, label: String): List<ClassWithImageEmbedded>

    @Transaction
    @Query(
        """
        select * from classes where label = :label order by order_key asc
    """
    )
    fun getPagingClasses(label: String): PagingSource<Int, ClassWithImageEmbedded>


    @Transaction
    @Query(
        """
        select * from families where label = :label
        order by order_key asc limit :limit
    """
    )
    suspend fun getFamilies(limit: Int, label: String): List<FamilyWithImageEmbedded>

    @Transaction
    @Query(
        """
        select * from families where label = :label
        order by order_key asc
    """
    )
    fun getPagingFamilies(label: String): PagingSource<Int, FamilyWithImageEmbedded>


    @Transaction
    @Query("""
        select * from habitatcategories where label = :label order by id asc limit :limit
    """)
    suspend fun getHabitatCategories(limit: Int, label: String): List<HabitatWithImageEmbedded>


    @Transaction
    @Query(
        """
        select * from orders where label = :label
        order by order_key asc limit :limit
    """
    )
    suspend fun getOrders(limit: Int, label: String): List<OrderWithImageEmbedded>


    @Transaction
    @Query(
        """
        select * from orders where label = :label
        order by order_key asc
    """
    )
    fun getPagingOrders(label: String): PagingSource<Int, OrderWithImageEmbedded>

    @Transaction
    @Query("""
        select * from habitatcategories where label = :label order by id asc
    """)
    fun getPagingHabitats(label: String): PagingSource<Int, HabitatWithImageEmbedded>

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


    @Query("delete from phylums where label = :label")
    suspend fun deletePhylums(label: String)

    @Query("delete from classes where label = :label")
    suspend fun deleteClasses(label: String)

    @Query("delete from orders where label = :label")
    suspend fun deleteOrders(label: String)

    @Query("delete from families where label = :label")
    suspend fun deleteFamilies(label: String)

    @Query("delete from habitatcategories where label = :label")
    suspend fun deleteHabitats(label: String)
}