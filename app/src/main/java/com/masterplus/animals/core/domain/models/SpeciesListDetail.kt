package com.masterplus.animals.core.domain.models

data class SpeciesListDetail(
    val id: Int,
    val introduction: String,
    val name: String,
    val scientificName: String,
    val familyId: Int,
    val recognitionAndInteraction: Int?,
    val habitatCategoryIds: List<Int>,
    val images: List<SpeciesImageModel>,
    val isFavorited: Boolean,
    val isListSelected: Boolean,
){
    val imageUrls get() = images.map { it.image.imageUrl }
}
