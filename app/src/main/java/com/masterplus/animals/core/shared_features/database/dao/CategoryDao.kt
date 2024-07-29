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
        select * from classes order by id asc limit :limit
    """)
    suspend fun getClasses(limit: Int): List<ClassEntity>


    @Query("""
        select * from classes order by id asc
    """)
    fun getPagingClasses(): PagingSource<Int, ClassEntity>



    @Query("""
        select * from families order by id asc limit :limit
    """)
    suspend fun getFamilies(limit: Int): List<FamilyEntity>

    @Query("""
        select * from families where order_id = :orderId order by id asc
    """)
    fun getPagingFamiliesWithOrderId(orderId: Int): PagingSource<Int, FamilyEntity>

    @Query("""
        select * from families order by id asc
    """)
    fun getPagingFamilies(): PagingSource<Int, FamilyEntity>



    @Query("""
        select * from habitatcategories order by id asc limit :limit
    """)
    suspend fun getHabitatCategories(limit: Int): List<HabitatCategoryEntity>



    @Query("""
        select * from orders order by id asc limit :limit
    """)
    suspend fun getOrders(limit: Int): List<OrderEntity>

    @Query("""
        select * from orders where class_id = :classId order by id asc
    """)
    fun getPagingOrdersWithClassId(classId: Int): PagingSource<Int, OrderEntity>

    @Query("""
        select * from orders order by id asc
    """)
    fun getPagingOrders(): PagingSource<Int, OrderEntity>
}