package com.masterplus.animals.core.domain.models

data class SpeciesModel(
    val id: Int,
    val introduction: String,
    val name: String,
    val scientificName: String,
    val genusId: Int,
    val recognitionAndInteraction: Int?,
)
