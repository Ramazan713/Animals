package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.models.SpeciesImageModel
import com.masterplus.animals.core.shared_features.database.entity.SpeciesImageEntity

fun SpeciesImageEntity.toSpeciesImage(): SpeciesImageModel {
    return SpeciesImageModel(
        id = id,
        speciesId = species_id,
        name = name,
        imagePath = image_path,
        imageUrl = image_url,
        imageOrder = image_order,
        createdAt = created_at,
        updatedAt = updated_at
    )
}
