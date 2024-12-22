package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Species",
    foreignKeys = [
        ForeignKey(
            entity = FamilyEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("family_id"),
        ),
        ForeignKey(
            entity = KingdomEntity::class,
            parentColumns = ["id"],
            childColumns = ["kingdom_id"],
        ),
    ]
)
data class SpeciesEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val scientific_name: String = "",
    val name_en: String = "",
    val name_tr: String = "",
    val family_id: Int,
    val kingdom_id: Int,
    val introduction_en: String = "",
    val introduction_tr: String = "",
    val recognition_and_interaction: Int?,
    val updated_at: String,
    val created_at: String
)
