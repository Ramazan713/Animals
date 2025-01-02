package com.masterplus.animals.core.domain.models

import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType


data class CategoryData(
    override val id: Int,
    val orderKey: Int,
    val image: ImageWithMetadata?,
    val title: String,
    val categoryType: CategoryType,
    val kingdomType: KingdomType,
    val secondaryTitle: String? = null
): Item{
    val imageUrl get() = image?.imageUrl
}
