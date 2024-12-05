package com.masterplus.animals.core.domain.models

import com.masterplus.animals.core.domain.enums.KingdomType

data class GenusModel(
    val id: Int,
    val scientificName: String,
    val genus: String,
    val familyId: Int,
    val kingdomType: KingdomType,
)
