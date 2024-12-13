package com.masterplus.animals.core.shared_features.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.masterplus.animals.core.shared_features.database.entity.HabitatCategoryEntity
import com.masterplus.animals.core.shared_features.database.entity_helper.ClassWithImageEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.FamilyWithImageEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.OrderWithImageEmbedded

@Dao
interface CategoryDao {
    @Query("""
        select * from classes where id = :classId
    """)
    suspend fun getClassWithId(classId: Int): ClassWithImageEmbedded?

    @Query("""
        select * from orders where id = :orderId
    """)
    suspend fun getOrderWithId(orderId: Int): OrderWithImageEmbedded?

    @Query("""
        select * from families where id = :familyId
    """)
    suspend fun getFamilyWithId(familyId: Int): FamilyWithImageEmbedded?

    @Query("""
        select * from habitatcategories where id = :habitatCategoryId
    """)
    suspend fun getHabitatCategoryWithId(habitatCategoryId: Int): HabitatCategoryEntity?



    @Query("""
        select * from classes where kingdom_id = :kingdomId
        order by id asc limit :limit
    """)
    suspend fun getClasses(limit: Int, kingdomId: Int): List<ClassWithImageEmbedded>


    @Query("""
        select * from classes where kingdom_id = :kingdomId order by id asc
    """)
    fun getPagingClasses(kingdomId: Int): PagingSource<Int, ClassWithImageEmbedded>



    @Query("""
        select * from families where kingdom_id = :kingdomId
        order by id asc limit :limit
    """)
    suspend fun getFamilies(limit: Int, kingdomId: Int): List<FamilyWithImageEmbedded>

    @Query("""
        select * from families where kingdom_id = :kingdomId and order_id = :orderId order by id asc
    """)
    fun getPagingFamiliesWithOrderId(orderId: Int, kingdomId: Int): PagingSource<Int, FamilyWithImageEmbedded>

    @Query("""
        select * from families where kingdom_id = :kingdomId
        order by id asc
    """)
    fun getPagingFamilies(kingdomId: Int): PagingSource<Int, FamilyWithImageEmbedded>



    @Query("""
        select * from habitatcategories order by id asc limit :limit
    """)
    suspend fun getHabitatCategories(limit: Int): List<HabitatCategoryEntity>



    @Query("""
        select * from orders where kingdom_id = :kingdomId
        order by id asc limit :limit
    """)
    suspend fun getOrders(limit: Int, kingdomId: Int): List<OrderWithImageEmbedded>

    @Query("""
        select * from orders where kingdom_id = :kingdomId and class_id = :classId order by id asc
    """)
    fun getPagingOrdersWithClassId(classId: Int, kingdomId: Int): PagingSource<Int, OrderWithImageEmbedded>

    @Query("""
        select * from orders where kingdom_id = :kingdomId 
        order by id asc
    """)
    fun getPagingOrders(kingdomId: Int): PagingSource<Int, OrderWithImageEmbedded>
}