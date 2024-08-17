package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.models.GenusModel
import com.masterplus.animals.core.shared_features.database.entity.GenusEntity
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum

fun GenusEntity.toGenus(
    language: LanguageEnum
): GenusModel {
    return GenusModel(
        id = id,
        scientificName = scientific_name,
        genus = if(language.isEn) genus_en else genus_tr,
        familyId = family_id
    )
}
