package com.masterplus.animals.features.category_list.presentation

import com.masterplus.animals.core.domain.models.CategoryData

sealed interface CategoryListDialogEvent {
    data class ShowEditSavePoint(
        val itemId: Int
    ): CategoryListDialogEvent

    data class ShowBottomSheet(
        val posIndex: Int,
        val item: CategoryData
    ): CategoryListDialogEvent
}