package com.masterplus.animals.core.shared_features.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.masterplus.animals.core.shared_features.database.entity.ClassEntity
import com.masterplus.animals.core.shared_features.database.entity.FamilyEntity
import com.masterplus.animals.core.shared_features.database.entity.HabitatCategoryEntity
import com.masterplus.animals.core.shared_features.database.entity.OrderEntity
import com.masterplus.animals.core.shared_features.database.entity.SpeciesEntity

@Dao
interface SavePointPosDao {

    @Query("select * from species where order_key = :orderKey and label = :label limit 1")
    suspend fun getSpeciesByOrderKeyAndLabel(orderKey: Int, label: String): SpeciesEntity?

    @Query("""
        select * from classes where order_key = :orderKey and label = :label limit 1
    """)
    suspend fun getClassWithOrderKey(orderKey: Int, label: String): ClassEntity?


    @Query("""
        select * from orders where order_key = :orderKey and label = :label limit 1
    """)
    suspend fun getOrderWithOrderKey(orderKey: Int, label: String): OrderEntity?


    @Query("""
        select * from families where order_key = :orderKey and label = :label limit 1
    """)
    suspend fun getFamilyWithOrderKey(orderKey: Int, label: String): FamilyEntity?

    @Query("""
        select * from habitatcategories where id = :orderKey and label = :label limit 1
    """)
    suspend fun getHabitatCategoryWithOrderKey(orderKey: Int, label: String): HabitatCategoryEntity?



    @Query("SELECT COUNT(*) FROM species WHERE order_key < :orderKey and label = :label")
    suspend fun getSpeciesPosByLabel(orderKey: Int, label: String): Int?


    @Query("SELECT COUNT(*) FROM classes WHERE order_key < :orderKey and label = :label")
    suspend fun getClassPosByLabel(orderKey: Int, label: String): Int?


    @Query("SELECT COUNT(*) FROM orders WHERE order_key < :orderKey and label = :label")
    suspend fun getOrderPosByLabel(orderKey: Int, label: String): Int?


    @Query("SELECT COUNT(*) FROM families WHERE order_key < :orderKey and label = :label")
    suspend fun getFamilyPosByLabel(orderKey: Int, label: String): Int?

    @Query("SELECT COUNT(*) FROM habitatcategories WHERE id < :orderKey and label = :label")
    suspend fun getHabitatPosByLabel(orderKey: Int, label: String): Int?

}