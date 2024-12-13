package com.masterplus.animals.core.domain.models

import com.masterplus.animals.core.domain.enums.KingdomType

data class FamilyModel(
    val id: Int,
    val scientificName: String,
    val family: String,
    val orderId: Int,
    val kingdomType: KingdomType,
    val image: ImageWithMetadata?
)
