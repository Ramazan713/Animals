package com.masterplus.animals.core.presentation.mapper

import com.masterplus.animals.core.domain.models.ClassModel
import com.masterplus.animals.core.presentation.models.ImageWithTitleModel

fun ClassModel.toImageWithTitleModel(): ImageWithTitleModel {
    return ImageWithTitleModel(
        id = id,
        imageUrl = image?.imageUrl,
        title = scientificName,
        subTitle = className,
        contentDescription = className,
    )
}
