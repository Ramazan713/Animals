package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity

@Entity(
    tableName = "Species",
    primaryKeys = ["id", "label"]
)
data class SpeciesEntity(
    val id: Int,
    val label: String,
    val order_key: Int,
    val scientific_name: String = "",
    val name_en: String = "",
    val name_tr: String = "",
    val family_id: Int,
    val order_id: Int,
    val class_id: Int,
    val phylum_id: Int,
    val kingdom_id: Int,
    val introduction_en: String = "",
    val introduction_tr: String = "",
    val recognition_and_interaction: Int?
)


