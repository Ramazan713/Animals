package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.domain.models.CategoryDataType
import com.masterplus.animals.core.domain.models.FamilyModel
import com.masterplus.animals.core.shared_features.database.entity.FamilyEntity
import com.masterplus.animals.core.shared_features.database.entity_helper.FamilyWithImageEmbedded
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum


fun FamilyWithImageEmbedded.toFamily(
    language: LanguageEnum
): FamilyModel {
    return with(family){
        FamilyModel(
            id = id,
            scientificName = scientific_name,
            family = if(language.isEn) family_en else family_tr,
            orderId = order_id,
            image = image?.toImageWithMetadata(),
            kingdomType = KingdomType.fromKingdomId(kingdom_id),
            orderKey = order_key
        )
    }
}


fun FamilyEntity.toFamily(
    language: LanguageEnum
): FamilyModel {
    return FamilyModel(
        id = id,
        scientificName = scientific_name,
        family = if(language.isEn) family_en else family_tr,
        orderId = order_id,
        image = null,
        kingdomType = KingdomType.fromKingdomId(kingdom_id),
        orderKey = order_key
    )
}


fun FamilyModel.toCategoryData(): CategoryData {
    return CategoryData(
        id = id,
        image = image,
        title = scientificName,
        secondaryTitle = family,
        categoryDataType = CategoryDataType.Family,
        kingdomType = kingdomType,
        orderKey = orderKey
    )
}