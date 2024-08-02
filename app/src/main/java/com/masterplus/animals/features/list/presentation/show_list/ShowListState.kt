package com.masterplus.animals.features.list.presentation.show_list

import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.shared_features.list.domain.models.ListView
import com.masterplus.animals.features.list.presentation.show_list.ShowListDialogEvent


data class ShowListState(
    val items: List<ListView> = emptyList(),
    val message: UiText? = null,
    val showDialog: Boolean = false,
    val dialogEvent: ShowListDialogEvent? = null,
)
