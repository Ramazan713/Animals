package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.shared_features.database.entity.ClassEntity
import com.masterplus.animals.core.domain.models.ClassModel

fun ClassEntity.toClass(): ClassModel {
    return ClassModel(
        id = id,
        scientificName = scientific_name,
        className = class_tr,
        phylumId = phylum_id,
        imagePath = image_path,
        imageUrl = image_url
    )
}
