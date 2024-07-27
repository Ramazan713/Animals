package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.models.AnimalImageModel
import com.masterplus.animals.core.shared_features.database.entity.AnimalImageEntity

fun AnimalImageEntity.toAnimalImage(): AnimalImageModel {
    return AnimalImageModel(
        id = id,
        animalId = animal_id,
        name = name,
        imagePath = image_path,
        imageUrl = image_url,
        imageOrder = image_order,
        createdAt = created_at,
        updatedAt = updated_at
    )
}
