package com.masterplus.animals.core.presentation.models

import com.masterplus.animals.core.domain.models.ImageWithMetadata

data class ImageWithTitleModel(
    val id: Int?,
    val image: ImageWithMetadata?,
    val title: String,
    val contentDescription: String? = null,
    val subTitle: String? = null,
)

