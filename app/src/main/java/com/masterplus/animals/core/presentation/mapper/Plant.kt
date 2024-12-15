package com.masterplus.animals.core.presentation.mapper

import com.masterplus.animals.core.domain.models.Plant
import com.masterplus.animals.core.presentation.models.ImageWithTitleModel

fun Plant.toImageWithTitleModel(): ImageWithTitleModel? {
    return ImageWithTitleModel(
        id = species.id,
        image = images.firstOrNull()?.image,
        title = species.name,
        subTitle = species.scientificName,
        contentDescription = species.name,
    )
}
