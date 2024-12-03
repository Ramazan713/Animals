package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.models.PhylumModel
import com.masterplus.animals.core.shared_features.database.entity.PhylumEntity
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum


fun PhylumEntity.toPhylumModel(
    language: LanguageEnum
): PhylumModel{
    return PhylumModel(
        id = id,
        scientificName = scientific_name,
        phylum = if(language.isEn) phylum_en else phylum_tr,
        kingdomId = kingdom_id,
        imagePath = image_path,
        imageUrl = image_url
    )
}