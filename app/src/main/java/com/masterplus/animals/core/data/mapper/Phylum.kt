package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.PhylumModel
import com.masterplus.animals.core.shared_features.database.entity.PhylumEntity
import com.masterplus.animals.core.shared_features.database.entity_helper.PhylumWithImageEmbedded
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum


fun PhylumWithImageEmbedded.toPhylumModel(
    language: LanguageEnum
): PhylumModel{
    return with(phylum){
        PhylumModel(
            id = id,
            scientificName = scientific_name,
            phylum = if(language.isEn) phylum_en else phylum_tr,
            kingdomType = KingdomType.fromKingdomId(kingdom_id),
            image = image?.toImageWithMetadata(),
        )
    }
}

fun PhylumEntity.toPhylumModel(
    language: LanguageEnum
): PhylumModel{
    return PhylumModel(
        id = id,
        scientificName = scientific_name,
        phylum = if(language.isEn) phylum_en else phylum_tr,
        kingdomType = KingdomType.fromKingdomId(kingdom_id),
        image = null,
    )
}