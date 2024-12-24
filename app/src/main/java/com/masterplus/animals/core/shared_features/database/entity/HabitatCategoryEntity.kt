package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "HabitatCategories",
    foreignKeys = [
        ForeignKey(
            entity = ImageEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("image_id"),
        )
    ],
    primaryKeys = ["id", "label"]
)
data class HabitatCategoryEntity(
    val id: Int,
    val label: String,
    val habitat_category_en: String,
    val habitat_category_tr: String,
    val image_id: Int?,
)
