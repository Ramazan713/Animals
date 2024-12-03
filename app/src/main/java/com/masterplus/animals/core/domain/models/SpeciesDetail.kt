package com.masterplus.animals.core.domain.models

data class SpeciesDetail(
    val id: Int,
    val introduction: String,
    val name: String,
    val scientificName: String,
    val genusId: Int,
    val recognitionAndInteraction: Int?,
    val habitatCategoryIds: List<Int>,
    val images: List<SpeciesImageModel>,
    val isFavorited: Boolean,
    val isListSelected: Boolean,
){
    val imageUrls get() = images.map { it.imageUrl }
}
