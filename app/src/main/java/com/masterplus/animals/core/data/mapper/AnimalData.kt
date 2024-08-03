package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.models.AnimalData
import com.masterplus.animals.core.shared_features.database.entity_helper.AnimalDataDetailEmbedded


fun AnimalDataDetailEmbedded.toAnimalData(): AnimalData{
    return AnimalData(
        id = animalData.id,
        name = animalData.name_tr,
        introduction = animalData.introduction_tr,
        imageUrls = images.map { it.image_url },
        scientificName = species.scientific_name,
        isFavorited = listsIsRemovable.any { !it },
        isListSelected = listsIsRemovable.any { it }
    )
}