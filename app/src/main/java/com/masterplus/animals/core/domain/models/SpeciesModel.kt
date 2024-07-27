package com.masterplus.animals.core.domain.models

data class SpeciesModel(
    val id: Int?,
    val scientificName: String,
    val species: String,
    val genusId: Int
)
