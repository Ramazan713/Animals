package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "HabitatCategories"
)
data class HabitatCategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val habitat_category_en: String,
    val habitat_category_tr: String
)
