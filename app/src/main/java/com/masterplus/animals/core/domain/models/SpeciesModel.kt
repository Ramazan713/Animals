package com.masterplus.animals.core.domain.models

import com.masterplus.animals.core.domain.enums.KingdomType

data class SpeciesModel(
    val id: Int,
    val introduction: String,
    val name: String,
    val scientificName: String,
    val kingdomType: KingdomType,
    val genusId: Int,
    val recognitionAndInteraction: Int?,
)
