package com.masterplus.animals.core.domain.models

data class GenusModel(
    val id: Int?,
    val scientificName: String,
    val genus: String,
    val familyId: Int
)
