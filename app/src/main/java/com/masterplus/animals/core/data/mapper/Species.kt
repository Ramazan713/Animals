package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.ISpeciesType
import com.masterplus.animals.core.domain.models.SpeciesImageModel
import com.masterplus.animals.core.domain.models.SpeciesModel
import com.masterplus.animals.core.domain.models.SpeciesWithImages
import com.masterplus.animals.core.presentation.models.ImageWithTitleModel
import com.masterplus.animals.core.shared_features.database.entity.SpeciesEntity
import com.masterplus.animals.core.shared_features.database.entity_helper.SpeciesImageWithMetadataEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.SpeciesWithImagesEmbedded
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum

fun SpeciesEntity.toSpecies(
    language: LanguageEnum
): SpeciesModel {
    val isEn = language.isEn
    return SpeciesModel(
        id = id,
        name = if(isEn) name_en else name_tr,
        introduction = if(isEn) introduction_en else introduction_tr,
        scientificName = scientific_name,
        familyId = family_id,
        recognitionAndInteraction = recognition_and_interaction,
        kingdomType = KingdomType.fromKingdomId(kingdom_id),
        orderId = order_id,
        phylumId = phylum_id,
        classId = class_id,
        orderKey = order_key
    )
}

fun SpeciesWithImagesEmbedded.toSpeciesWithImages(language: LanguageEnum): SpeciesWithImages {
    return SpeciesWithImages(
        species = species.toSpecies(language),
        images = images.map { it.toSpeciesImageModel() }
    )
}

fun ISpeciesType.toImageWithTitleModel(): ImageWithTitleModel{
    return ImageWithTitleModel(
        id = id,
        image = images.firstOrNull()?.image,
        title = species.scientificName,
        subTitle = species.name
    )
}
