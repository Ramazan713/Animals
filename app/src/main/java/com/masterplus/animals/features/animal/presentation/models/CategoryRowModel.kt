package com.masterplus.animals.features.animal.presentation.models

import com.masterplus.animals.core.presentation.models.ImageWithTitleModel

data class CategoryRowModel(
    val imageWithTitleModels: List<ImageWithTitleModel> = emptyList(),
    val showMore: Boolean = false
)
