package com.masterplus.animals.features.category_list.presentation

sealed interface CategoryAction {

    data class ShowDialog(val dialogEvent: CategoryListDialogEvent?): CategoryAction
}