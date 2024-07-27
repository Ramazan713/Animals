package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Species",
    foreignKeys = [
        ForeignKey(
            entity = GenusEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("genus_id"),
        )
    ]
)
data class SpeciesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val scientific_name: String,
    val species_en: String,
    val species_tr: String,
    val genus_id: Int
)
