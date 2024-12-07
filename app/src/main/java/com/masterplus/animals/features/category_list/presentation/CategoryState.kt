package com.masterplus.animals.features.category_list.presentation

import com.masterplus.animals.R
import com.masterplus.animals.core.domain.enums.KingdomType

data class CategoryState(
    val isLoading: Boolean = false,
    val title: String = "",
    val collectionName: String = "",
    val subTitle: String? = null,
    val parentImageData: Any? = R.drawable.animals_plants,
    val kingdomType: KingdomType
)
