package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.models.SpeciesModel
import com.masterplus.animals.core.shared_features.database.entity.SpeciesEntity
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum

fun SpeciesEntity.toSpecies(
    language: LanguageEnum
): SpeciesModel {
    val isEn = language.isEn
    return SpeciesModel(
        id = id,
        name = if(isEn) name_en else name_tr,
        introduction = if(isEn) introduction_en else introduction_tr,
        scientificName = scientific_name,
        genusId = genus_id,
        recognitionAndInteraction = recognition_and_interaction
    )
}
