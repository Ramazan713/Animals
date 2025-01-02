package com.masterplus.animals.core.shared_features.database.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface SavePointPosDao {

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