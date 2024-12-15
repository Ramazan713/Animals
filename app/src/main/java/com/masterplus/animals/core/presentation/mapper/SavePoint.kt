package com.masterplus.animals.core.presentation.mapper

import com.masterplus.animals.core.presentation.models.ImageWithTitleModel
import com.masterplus.animals.core.shared_features.savepoint.domain.models.SavePoint

fun SavePoint.toImageWithTitleModel(): ImageWithTitleModel {
    return ImageWithTitleModel(
        id = id,
        image = image,
        title = title,
    )
}