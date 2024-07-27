package com.masterplus.animals.core.presentation.models

data class ImageWithTitleModel(
    val id: Int?,
    val imageUrl: String,
    val title: String,
    val contentDescription: String? = null,
    val subTitle: String? = null,
)

