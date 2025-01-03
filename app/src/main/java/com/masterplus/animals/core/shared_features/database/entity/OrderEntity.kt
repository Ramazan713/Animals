package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "Orders",
    foreignKeys = [
        ForeignKey(
            entity = ImageEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("image_id"),
        )
    ],
    primaryKeys = ["id", "label"]
)
data class OrderEntity(
    val id: Int,
    val label: String,
    val order_key: Int,
    val scientific_name: String,
    val order_en: String,
    val order_tr: String,
    val class_id: Int,
    val kingdom_id: Int,
    val image_id: Int?
)
