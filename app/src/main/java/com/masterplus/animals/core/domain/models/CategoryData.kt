package com.masterplus.animals.core.domain.models

import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType


data class CategoryData(
    val id: Int?,
    val image: ImageWithMetadata?,
    val title: String,
    val categoryType: CategoryType,
    val kingdomType: KingdomType,
    val secondaryTitle: String? = null
){
    val imageUrl get() = image?.imageUrl
}
