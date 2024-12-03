package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "SpeciesHabitatCategories",
    foreignKeys = [
        ForeignKey(
            childColumns = ["species_id"],
            parentColumns = ["id"],
            entity = SpeciesEntity::class,
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            childColumns = ["category_id"],
            parentColumns = ["id"],
            entity = HabitatCategoryEntity::class,
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    primaryKeys = [
        "category_id","species_id"
    ]
)
data class SpeciesHabitatCategoryEntity(
    val category_id: Int,
    val species_id: Int
)
