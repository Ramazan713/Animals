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
        ),
        ForeignKey(
            entity = HabitatCategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["habitat_category_id"],
        ),
    ]
)
data class SpeciesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val scientific_name: String,
    val name_en: String = "",
    val name_tr: String = "",
    val genus_id: Int,
    val habitat_category_id: Int,
    val introduction_en: String = "",
    val introduction_tr: String = "",
    val recognition_and_interaction: Int?,
    val updated_at: String,
    val created_at: String
)
