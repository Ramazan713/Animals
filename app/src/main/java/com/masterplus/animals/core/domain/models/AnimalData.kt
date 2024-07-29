package com.masterplus.animals.core.domain.models

data class AnimalData(
    val id: Int?,
    val introduction: String,
    val name: String,
    val scientificName: String,
    val imageUrls: List<String>
)
