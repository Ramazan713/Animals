package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "Phylums",
    foreignKeys = [
        ForeignKey(
            entity = KingdomEntity::class,
            parentColumns = ["id"],
            childColumns = ["kingdom_id"]
        )
    ]
)
data class PhylumEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val scientific_name: String,
    val phylum_en: String,
    val phylum_tr: String,
    val kingdom_id: Int
)
