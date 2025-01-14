package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "Families",
    foreignKeys = [
        ForeignKey(
            entity = ImageEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("image_id"),
        )
    ],
    primaryKeys = ["id", "label"]
)
data class FamilyEntity(
    val id: Int,
    val label: String,
    val order_key: Int,
    val scientific_name: String,
    val family_en: String,
    val family_tr: String,
    val order_id: Int,
    val kingdom_id: Int,
    val image_id: Int?
)
