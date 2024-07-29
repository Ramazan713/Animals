package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.models.AnimalData
import com.masterplus.animals.core.shared_features.database.entity_helper.AnimalDataWithImagesEntity


fun AnimalDataWithImagesEntity.toAnimalData(): AnimalData{
    return with(animalData){
        AnimalData(
            id = id,
            name = name_tr,
            introduction = introduction_tr,
            imageUrls = images.map { it.image_url },
            scientificName = species.scientific_name
        )
    }
}