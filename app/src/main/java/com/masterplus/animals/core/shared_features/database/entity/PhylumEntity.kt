package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "Phylums",
    foreignKeys = [
        ForeignKey(
            entity = ImageEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("image_id"),
        )
    ],
    primaryKeys = ["id", "label"]
)
data class PhylumEntity(
    val id: Int,
    val label: String,
    val order_key: Int,
    val scientific_name: String,
    val phylum_en: String,
    val phylum_tr: String,
    val kingdom_id: Int,
    val image_id: Int?
)
