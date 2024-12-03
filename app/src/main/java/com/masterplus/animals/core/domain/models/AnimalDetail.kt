package com.masterplus.animals.core.domain.models

data class AnimalDetail(
    val animal: Animal,
    val phylum: PhylumModel,
    val classModel: ClassModel,
    val order: OrderModel,
    val family: FamilyModel,
    val genus: GenusModel,
    val species: SpeciesModel,
    val habitatCategories: List<HabitatCategoryModel>,
    val images: List<SpeciesImageModel>
)
