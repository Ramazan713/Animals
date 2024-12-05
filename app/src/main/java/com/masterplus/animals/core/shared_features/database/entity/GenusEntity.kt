package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Genuses",
    foreignKeys = [
        ForeignKey(
            entity = FamilyEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("family_id"),
        ),
        ForeignKey(
            entity = KingdomEntity::class,
            parentColumns = ["id"],
            childColumns = ["kingdom_id"]
        )
    ]
)
data class GenusEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val scientific_name: String,
    val genus_en: String,
    val genus_tr: String,
    val family_id: Int,
    val kingdom_id: Int,
    val created_at: String,
    val updated_at: String
)
