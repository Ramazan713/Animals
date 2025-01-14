package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.domain.models.CategoryDataType
import com.masterplus.animals.core.domain.models.SpeciesListDetail
import com.masterplus.animals.core.shared_features.database.entity_helper.SpeciesDetailEmbedded
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum


fun SpeciesDetailEmbedded.toSpeciesListDetail(
    language: LanguageEnum
): SpeciesListDetail{
    val isEn = language.isEn
    return with(species){
        SpeciesListDetail(
            id = id,
            name = if(isEn) name_en else name_tr,
            introduction = if(isEn) introduction_en else introduction_tr,
            scientificName = scientific_name,
            familyId = family_id,
            recognitionAndInteraction = recognition_and_interaction,
            habitatCategoryIds = habitatCategories.map { it.id },
            isFavorited = listsIsRemovable.any { !it },
            isListSelected = listsIsRemovable.any { it },
            images = images.map { it.toSpeciesImageModel() },
            kingdomType = KingdomType.fromKingdomId(kingdom_id),
            orderKey = order_key
        )
    }
}

fun SpeciesListDetail.toCategoryData(): CategoryData{
    return CategoryData(
        id = id,
        title = name,
        categoryDataType = CategoryDataType.Order,
        kingdomType = kingdomType,
        orderKey = orderKey,
        image = images.firstOrNull()?.image,
        secondaryTitle = scientificName
    )
}
