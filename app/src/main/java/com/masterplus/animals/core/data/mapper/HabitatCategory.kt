package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.models.HabitatCategoryModel
import com.masterplus.animals.core.shared_features.database.entity.HabitatCategoryEntity

fun HabitatCategoryEntity.toHabitatCategory(): HabitatCategoryModel {
    return HabitatCategoryModel(
        id = id,
        habitatCategory = habitat_category_tr
    )
}
