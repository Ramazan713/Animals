package com.masterplus.animals.core.presentation.mapper

import com.masterplus.animals.core.domain.models.OrderModel
import com.masterplus.animals.core.presentation.models.ImageWithTitleModel

fun OrderModel.toImageWithTitleModel(): ImageWithTitleModel {
    return ImageWithTitleModel(
        id = id,
        imageUrl = imageUrl,
        title = scientificName,
        subTitle = order,
        contentDescription = order,
    )
}
