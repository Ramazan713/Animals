package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.SpeciesListDetail
import com.masterplus.animals.core.shared_features.database.entity_helper.SpeciesDetailEmbedded
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum


fun SpeciesDetailEmbedded.toSpeciesListDetail(
    language: LanguageEnum
): SpeciesListDetail{
    val isEn = language.isEn
    return with(species){
        SpeciesListDetail(
            id = id,
            name = if(isEn) name_en else name_tr,
            introduction = if(isEn) introduction_en else introduction_tr,
            scientificName = scientific_name,
            familyId = family_id,
            recognitionAndInteraction = recognition_and_interaction,
            habitatCategoryIds = habitatCategories.mapNotNull { it.id },
            isFavorited = listsIsRemovable.any { !it },
            isListSelected = listsIsRemovable.any { it },
            images = images.map { it.toSpeciesImageModel() },
            kingdomType = KingdomType.fromKingdomId(kingdom_id)
        )
    }
}