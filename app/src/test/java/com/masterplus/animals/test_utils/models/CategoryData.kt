package com.masterplus.animals.test_utils.models

import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.domain.models.ImageWithMetadata
import com.masterplus.animals.core.presentation.utils.SampleDatas

fun categoryData(
    id: Int? = 1,
    image: ImageWithMetadata? = SampleDatas.imageWithMetadata,
    title: String = "Category $id",
    categoryType: CategoryType = CategoryType.Class,
    secondaryTitle: String? = null,
    kingdomType: KingdomType = KingdomType.DEFAULT
): CategoryData {
    return CategoryData(
        id, image, title, categoryType, kingdomType, secondaryTitle
    )
}
