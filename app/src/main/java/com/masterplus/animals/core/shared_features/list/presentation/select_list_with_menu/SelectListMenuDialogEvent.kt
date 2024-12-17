package com.masterplus.animals.core.shared_features.list.presentation.select_list_with_menu

sealed interface SelectListMenuDialogEvent{
    data class AskFavoriteDelete(val speciesId: Int): SelectListMenuDialogEvent
}
