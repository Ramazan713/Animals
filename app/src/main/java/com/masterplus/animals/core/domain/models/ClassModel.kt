package com.masterplus.animals.core.domain.models

import com.masterplus.animals.core.domain.enums.KingdomType

data class ClassModel(
    val id: Int,
    val orderKey: Int,
    val scientificName: String,
    val className: String,
    val phylumId: Int,
    val kingdomType: KingdomType,
    val image: ImageWithMetadata?
)
