package com.masterplus.animals.core.presentation.models

import com.masterplus.animals.core.domain.models.CategoryData

data class CategoryDataRowModel(
    val categoryDataList: List<CategoryData> = emptyList(),
    val showMore: Boolean = false
)

data class CategoryRowModel(
    val imageWithTitleModels: List<ImageWithTitleModel> = emptyList(),
    val showMore: Boolean = false
)
