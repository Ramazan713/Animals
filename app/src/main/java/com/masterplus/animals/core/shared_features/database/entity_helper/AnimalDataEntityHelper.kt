package com.masterplus.animals.core.shared_features.database.entity_helper

data class AnimalDataEntityHelper(
    val id: Int?,
    val introduction_en: String = "",
    val introduction_tr: String = "",
    val name_en: String = "",
    val name_tr: String = "",
    val species_id: Int
)
