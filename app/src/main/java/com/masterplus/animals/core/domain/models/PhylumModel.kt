package com.masterplus.animals.core.domain.models

import com.masterplus.animals.core.domain.enums.KingdomType

data class PhylumModel(
    val id: Int,
    val scientificName: String,
    val phylum: String,
    val kingdomType: KingdomType,
    val imagePath: String?,
    val imageUrl: String?
)
