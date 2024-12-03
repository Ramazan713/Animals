package com.masterplus.animals.core.domain.models

data class SpeciesImageModel(
    val id: Int?,
    val speciesId: Int,
    val name: String?,
    val imagePath: String,
    val imageUrl: String?,
    val imageOrder: Int,
    val createdAt: String,
    val updatedAt: String
)
