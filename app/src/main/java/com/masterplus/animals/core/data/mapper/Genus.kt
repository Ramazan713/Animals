package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.models.GenusModel
import com.masterplus.animals.core.shared_features.database.entity.GenusEntity

fun GenusEntity.toGenus(): GenusModel {
    return GenusModel(
        id = id,
        scientificName = scientific_name,
        genus = genus_tr,
        familyId = family_id
    )
}
