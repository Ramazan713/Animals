package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.models.SpeciesDetail
import com.masterplus.animals.core.shared_features.database.entity_helper.SpeciesDetailEmbedded


fun SpeciesDetailEmbedded.toSpeciesDetail(): SpeciesDetail{
    return with(species){
        SpeciesDetail(
            id = id,
            name = name_tr,
            introduction = introduction_tr,
            scientificName = scientific_name,
            genusId = genus_id,
            recognitionAndInteraction = recognition_and_interaction,
            habitatCategoryId = habitat_category_id,
            isFavorited = listsIsRemovable.any { !it },
            isListSelected = listsIsRemovable.any { it },
            images = images.map { it.toAnimalImage() }
        )
    }
}