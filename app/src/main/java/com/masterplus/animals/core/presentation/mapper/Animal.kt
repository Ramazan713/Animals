package com.masterplus.animals.core.presentation.mapper

import com.masterplus.animals.core.domain.models.Animal
import com.masterplus.animals.core.presentation.models.ImageWithTitleModel

fun Animal.toImageWithTitleModel(): ImageWithTitleModel? {
    return ImageWithTitleModel(
        id = species.id,
        imageUrl = imageUrls.firstOrNull() ?: "",
        title = species.name,
        subTitle = species.scientificName,
        contentDescription = species.name,
    )
}