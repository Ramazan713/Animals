package com.masterplus.animals.features.category_list.presentation

import com.masterplus.animals.core.domain.models.CategoryData

sealed interface CategoryListDialogEvent {
    data class ShowEditSavePoint(
        val orderKey: Int
    ): CategoryListDialogEvent

    data class ShowBottomSheet(
        val item: CategoryData
    ): CategoryListDialogEvent
}