package com.masterplus.animals.features.animal.presentation.models

import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.presentation.models.ImageWithTitleModel

data class CategoryDataRowModel(
    val categoryDatas: List<CategoryData> = emptyList(),
    val showMore: Boolean = false
)

data class CategoryRowModel(
    val imageWithTitleModels: List<ImageWithTitleModel> = emptyList(),
    val showMore: Boolean = false
)
