package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.models.FamilyModel
import com.masterplus.animals.core.shared_features.database.entity.FamilyEntity

fun FamilyEntity.toFamily(): FamilyModel {
    return FamilyModel(
        id = id,
        scientificName = scientific_name,
        family = family_tr,
        orderId = order_id,
        imagePath = image_path,
        imageUrl = image_url
    )
}
