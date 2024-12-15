package com.masterplus.animals.core.presentation.mapper

import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.models.Animal
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.presentation.models.ImageWithTitleModel

fun Animal.toImageWithTitleModel(): ImageWithTitleModel? {
    return ImageWithTitleModel(
        id = species.id,
        image = images.firstOrNull()?.image,
        title = species.name,
        subTitle = species.scientificName,
        contentDescription = species.name,
    )
}

