package com.masterplus.animals.core.domain.models

import com.masterplus.animals.core.domain.enums.KingdomType

data class OrderModel(
    val id: Int,
    val orderKey: Int,
    val scientificName: String,
    val order: String,
    val classId: Int,
    val kingdomType: KingdomType,
    val image: ImageWithMetadata?
)
