package com.masterplus.animals.test_utils.models

import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.models.CategoryData

fun categoryData(
    id: Int? = 1,
    imageUrl: String? = "https://example.com/image.jpg",
    title: String = "Category $id",
    categoryType: CategoryType = CategoryType.Class,
    secondaryTitle: String? = null
): CategoryData {
    return CategoryData(
        id, imageUrl, title, categoryType, secondaryTitle
    )
}
