package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.models.SpeciesDetail
import com.masterplus.animals.core.shared_features.database.entity_helper.SpeciesDetailEmbedded
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum


fun SpeciesDetailEmbedded.toSpeciesDetail(
    language: LanguageEnum
): SpeciesDetail{
    val isEn = language.isEn
    return with(species){
        SpeciesDetail(
            id = id,
            name = if(isEn) name_en else name_tr,
            introduction = if(isEn) introduction_en else introduction_tr,
            scientificName = scientific_name,
            genusId = genus_id,
            recognitionAndInteraction = recognition_and_interaction,
            habitatCategoryIds = habitatCategories.mapNotNull { it.id },
            isFavorited = listsIsRemovable.any { !it },
            isListSelected = listsIsRemovable.any { it },
            images = images.map { it.toSpeciesImage() }
        )
    }
}