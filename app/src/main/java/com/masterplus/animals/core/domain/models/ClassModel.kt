package com.masterplus.animals.core.domain.models

data class ClassModel(
    val id: Int?,
    val scientificName: String,
    val className: String,
    val phylumId: Int,
    val imagePath: String,
    val imageUrl: String
)
