package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "Phylums"
)
data class PhylumEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val scientific_name: String,
    val phylum_en: String,
    val phylum_tr: String
)
