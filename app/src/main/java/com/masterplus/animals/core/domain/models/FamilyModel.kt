package com.masterplus.animals.core.domain.models

data class FamilyModel(
    val id: Int,
    val scientificName: String,
    val family: String,
    val orderId: Int,
    val imagePath: String,
    val imageUrl: String
)
