package com.masterplus.animals.features.category_list.presentation

import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.ImageWithMetadata

data class CategoryState(
    val isLoading: Boolean = false,
    val showAllImageInHeader: Boolean = false,
    val title: String = "",
    val collectionName: String = "",
    val subTitle: String? = null,
    val parentImageData: ImageWithMetadata? = null,
    val kingdomType: KingdomType,
    val categoryType: CategoryType,
    val categoryItemId: Int?,
    val dialogEvent: CategoryListDialogEvent? = null,
)
