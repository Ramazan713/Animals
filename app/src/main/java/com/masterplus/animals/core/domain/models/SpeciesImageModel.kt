package com.masterplus.animals.core.domain.models

data class SpeciesImageModel(
    val speciesId: Int,
    val image: ImageWithMetadata,
    val imageOrder: Int
)
