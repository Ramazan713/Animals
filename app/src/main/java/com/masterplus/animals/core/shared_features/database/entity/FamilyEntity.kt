package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Families",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("order_id"),
        )
    ]
)
data class FamilyEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val scientific_name: String,
    val family_en: String,
    val family_tr: String,
    val order_id: Int,
    val image_path: String?,
    val image_url: String?,
    val created_at: String,
    val updated_at: String
)
