package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.domain.models.HabitatCategoryModel
import com.masterplus.animals.core.shared_features.database.entity.HabitatCategoryEntity
import com.masterplus.animals.core.shared_features.database.entity_helper.HabitatWithImageEmbedded
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum

fun HabitatCategoryEntity.toHabitatCategory(
    language: LanguageEnum
): HabitatCategoryModel {
    return HabitatCategoryModel(
        id = id,
        image = null,
        habitatCategory = if(language.isEn) habitat_category_en else habitat_category_tr
    )
}

fun HabitatWithImageEmbedded.toHabitatCategory(
    language: LanguageEnum
): HabitatCategoryModel {
    return with(habitat){
        HabitatCategoryModel(
            id = id,
            image = image?.toImageWithMetadata(),
            habitatCategory = if(language.isEn) habitat_category_en else habitat_category_tr
        )
    }
}


fun HabitatCategoryModel.toCategoryData(): CategoryData {
    return CategoryData(
        id = id,
        image = image,
        title = habitatCategory,
        categoryType = CategoryType.Habitat,
        kingdomType = KingdomType.DEFAULT,
        orderKey = id
    )
}