package com.masterplus.animals.core.presentation.mapper

import com.masterplus.animals.core.domain.models.AnimalData
import com.masterplus.animals.core.presentation.models.ImageWithTitleModel

fun AnimalData.toImageWithTitleModel(): ImageWithTitleModel? {
    return ImageWithTitleModel(
        id = id,
        imageUrl = imageUrls.firstOrNull() ?: "",
        title = name,
        subTitle = scientificName,
        contentDescription = name,
    )
}