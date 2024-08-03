package com.masterplus.animals.core.shared_features.list.presentation.select_list

import com.masterplus.animals.core.shared_features.list.domain.models.SelectableListView

data class SelectListState(
    val items: List<SelectableListView> = emptyList(),
    val listIdControl: Int? = null,
    val dialogEvent: SelectListDialogEvent? = null
)
