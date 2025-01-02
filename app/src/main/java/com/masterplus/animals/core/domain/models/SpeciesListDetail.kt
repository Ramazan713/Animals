package com.masterplus.animals.core.domain.models

import com.masterplus.animals.core.domain.enums.KingdomType

data class SpeciesListDetail(
    override val id: Int,
    val orderKey: Int,
    val introduction: String,
    val kingdomType: KingdomType,
    val name: String,
    val scientificName: String,
    val familyId: Int,
    val recognitionAndInteraction: Int?,
    val habitatCategoryIds: List<Int>,
    val images: List<SpeciesImageModel>,
    val isFavorited: Boolean,
    val isListSelected: Boolean,
): Item{
    val imageUrls get() = images.map { it.image.imageUrl }
}
