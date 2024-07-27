package com.masterplus.animals.core.domain.models

data class AnimalImageModel(
    val id: Int?,
    val animalId: Int,
    val name: String?,
    val imagePath: String,
    val imageUrl: String,
    val imageOrder: Int,
    val createdAt: String,
    val updatedAt: String
)
