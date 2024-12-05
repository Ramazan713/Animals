package com.masterplus.animals.core.shared_features.database.dao

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import com.masterplus.animals.core.shared_features.database.entity.ClassEntity
import com.masterplus.animals.core.shared_features.database.entity.FamilyEntity
import com.masterplus.animals.core.shared_features.database.entity.HabitatCategoryEntity
import com.masterplus.animals.core.shared_features.database.entity.OrderEntity

@Dao
interface CategoryDao {
    @Query("""
        select * from classes where id = :classId
    """)
    suspend fun getClassWithId(classId: Int): ClassEntity?

    @Query("""
        select * from orders where id = :orderId
    """)
    suspend fun getOrderWithId(orderId: Int): OrderEntity?

    @Query("""
        select * from families where id = :familyId
    """)
    suspend fun getFamilyWithId(familyId: Int): FamilyEntity?

    @Query("""
        select * from habitatcategories where id = :habitatCategoryId
    """)
    suspend fun getHabitatCategoryWithId(habitatCategoryId: Int): HabitatCategoryEntity?



    @Query("""
        select * from classes where kingdom_id = :kingdomId
        order by id asc limit :limit
    """)
    suspend fun getClasses(limit: Int, kingdomId: Int): List<ClassEntity>


    @Query("""
        select * from classes where kingdom_id = :kingdomId order by id asc
    """)
    fun getPagingClasses(kingdomId: Int): PagingSource<Int, ClassEntity>



    @Query("""
        select * from families where kingdom_id = :kingdomId
        order by id asc limit :limit
    """)
    suspend fun getFamilies(limit: Int, kingdomId: Int): List<FamilyEntity>

    @Query("""
        select * from families where kingdom_id = :kingdomId and order_id = :orderId order by id asc
    """)
    fun getPagingFamiliesWithOrderId(orderId: Int, kingdomId: Int): PagingSource<Int, FamilyEntity>

    @Query("""
        select * from families where kingdom_id = :kingdomId
        order by id asc
    """)
    fun getPagingFamilies(kingdomId: Int): PagingSource<Int, FamilyEntity>



    @Query("""
        select * from habitatcategories order by id asc limit :limit
    """)
    suspend fun getHabitatCategories(limit: Int): List<HabitatCategoryEntity>



    @Query("""
        select * from orders where kingdom_id = :kingdomId
        order by id asc limit :limit
    """)
    suspend fun getOrders(limit: Int, kingdomId: Int): List<OrderEntity>

    @Query("""
        select * from orders where kingdom_id = :kingdomId and class_id = :classId order by id asc
    """)
    fun getPagingOrdersWithClassId(classId: Int, kingdomId: Int): PagingSource<Int, OrderEntity>

    @Query("""
        select * from orders where kingdom_id = :kingdomId 
        order by id asc
    """)
    fun getPagingOrders(kingdomId: Int): PagingSource<Int, OrderEntity>
}