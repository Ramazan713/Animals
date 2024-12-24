package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "SpeciesImages",
    foreignKeys = [
        ForeignKey(
            entity = SpeciesEntity::class,
            parentColumns = arrayOf("id", "label"),
            childColumns = arrayOf("species_id", "label"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ImageEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("image_id"),
            onDelete = ForeignKey.CASCADE
        ),
    ],
    primaryKeys = ["label", "species_id", "image_id"]
)
data class SpeciesImageEntity(
    val label: String,
    val species_id: Int,
    val image_id: Int,
    val image_order: Int = 1
)
