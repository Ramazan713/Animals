package com.masterplus.animals.core.presentation.mapper

import com.masterplus.animals.core.domain.models.FamilyModel
import com.masterplus.animals.core.presentation.models.ImageWithTitleModel

fun FamilyModel.toImageWithTitleModel(): ImageWithTitleModel {
    return ImageWithTitleModel(
        id = id,
        imageUrl = imageUrl,
        title = scientificName,
        subTitle = family,
        contentDescription = family,
    )
}
