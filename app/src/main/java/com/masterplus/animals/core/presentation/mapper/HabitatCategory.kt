package com.masterplus.animals.core.presentation.mapper

import com.masterplus.animals.core.domain.models.HabitatCategoryModel
import com.masterplus.animals.core.presentation.models.ImageWithTitleModel

fun HabitatCategoryModel.toImageWithTitleModel(): ImageWithTitleModel {
    return ImageWithTitleModel(
        id = id,
        image = null,
        title = habitatCategory,
        contentDescription = habitatCategory,
    )
}
