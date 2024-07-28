package com.masterplus.animals.features.category_list.presentation

import com.masterplus.animals.R

data class CategoryState(
    val isLoading: Boolean = false,
    val title: String = "",
    val collectionName: String = "",
    val subTitle: String? = null,
    val parentImageData: Any? = R.drawable.all_animals,
)
