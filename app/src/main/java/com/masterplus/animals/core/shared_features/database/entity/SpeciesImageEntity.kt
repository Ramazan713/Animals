package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "SpeciesImages",
    foreignKeys = [
        ForeignKey(
            entity = SpeciesEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("species_id"),
        )
    ]
)
data class SpeciesImageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val species_id: Int,
    val name: String?,
    val image_path: String,
    val image_url: String,
    val image_order: Int,
    val created_at: String,
    val updated_at: String
)
