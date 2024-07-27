package com.masterplus.animals.core.domain.models

data class OrderModel(
    val id: Int?,
    val scientificName: String,
    val order: String,
    val classId: Int,
    val imagePath: String,
    val imageUrl: String
)
