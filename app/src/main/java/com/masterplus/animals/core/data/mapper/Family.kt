package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.domain.models.FamilyModel
import com.masterplus.animals.core.shared_features.database.entity.FamilyEntity
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum

fun FamilyEntity.toFamily(
    language: LanguageEnum
): FamilyModel {
    return FamilyModel(
        id = id,
        scientificName = scientific_name,
        family = if(language.isEn) family_en else family_tr,
        orderId = order_id,
        imagePath = image_path,
        imageUrl = image_url,
        kingdomType = KingdomType.fromKingdomId(kingdom_id)
    )
}


fun FamilyModel.toCategoryData(): CategoryData {
    return CategoryData(
        id = id,
        imageUrl = imageUrl,
        title = scientificName,
        secondaryTitle = family,
        categoryType = CategoryType.Family
    )
}