package com.masterplus.animals.core.shared_features.list.presentation.select_list_with_menu

import com.masterplus.animals.core.shared_features.list.domain.enums.SelectListMenuEnum

data class SelectListMenuState(
    val listMenuItems: List<SelectListMenuEnum> = emptyList(),
    val isFavorite: Boolean = false,
    val isAddedToList: Boolean = false,
    val listIdControl: Int? = null,
    val dialogEvent: SelectListMenuDialogEvent? = null
)
