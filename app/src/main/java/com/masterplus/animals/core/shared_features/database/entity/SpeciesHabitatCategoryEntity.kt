package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity

@Entity(
    tableName = "SpeciesHabitatCategories",
    primaryKeys = [
        "category_id","species_id"
    ]
)
data class SpeciesHabitatCategoryEntity(
    val category_id: Int,
    val species_id: Int
)
