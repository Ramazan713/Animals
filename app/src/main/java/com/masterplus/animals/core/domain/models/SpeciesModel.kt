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


interface SpeciesDetail{
    val detail: ISpeciesType
    val phylum: PhylumModel
    val classModel: ClassModel
    val order: OrderModel
    val family: FamilyModel
    val genus: GenusModel
    val species: SpeciesModel
    val habitatCategories: List<HabitatCategoryModel>
    val images: List<SpeciesImageModel>
    val isFavorited: Boolean
    val isListSelected: Boolean
}

interface ISpeciesType {
    val id: Int?
    val species: SpeciesModel
    val images: List<SpeciesImageModel>
    val imageUrls get() = images.map { it.image.imageUrl }
}