package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.masterplus.animals.core.shared_features.database.entity.ClassEntity

@Entity(
    tableName = "Orders",
    foreignKeys = [
        ForeignKey(
            entity = ClassEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("class_id"),
        )
    ]
)
data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val scientific_name: String,
    val order_en: String,
    val order_tr: String,
    val class_id: Int,
    val image_path: String,
    val image_url: String
)