package com.masterplus.animals.core.shared_features.list.presentation.select_list_with_menu


sealed interface SelectListMenuAction{
    data class LoadData(val animalId: Int, val listId: Int?): SelectListMenuAction

    data class AddToFavorite(val animalId: Int): SelectListMenuAction

    data class AddOrAskFavorite(val animalId: Int): SelectListMenuAction

    data class ShowDialog(val dialogEvent: SelectListMenuDialogEvent?): SelectListMenuAction
}
