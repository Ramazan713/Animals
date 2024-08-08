package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.models.SpeciesModel
import com.masterplus.animals.core.shared_features.database.entity.SpeciesEntity

fun SpeciesEntity.toSpecies(): SpeciesModel {
    return SpeciesModel(
        id = id,
        name = name_tr,
        introduction = introduction_tr,
        scientificName = scientific_name,
        genusId = genus_id,
        recognitionAndInteraction = recognition_and_interaction,
        habitatCategoryId = habitat_category_id
    )
}
