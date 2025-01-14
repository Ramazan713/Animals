package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "Classes",
    foreignKeys = [
        ForeignKey(
            entity = ImageEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("image_id"),
        )
    ],
    primaryKeys = ["id", "label"]
)
data class ClassEntity(
    val id: Int,
    val label: String,
    val order_key: Int,
    val scientific_name: String,
    val class_en: String,
    val class_tr: String,
    val phylum_id: Int,
    val kingdom_id: Int,
    val image_id: Int?
)
