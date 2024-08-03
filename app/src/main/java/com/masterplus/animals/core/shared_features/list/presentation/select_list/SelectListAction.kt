package com.masterplus.animals.core.shared_features.list.presentation.select_list

import com.masterplus.animals.core.shared_features.list.domain.models.ListView
import com.masterplus.animals.core.shared_features.list.domain.models.SelectableListView

sealed interface SelectListAction{

    data class LoadData(val animalId: Int, val listIdControl: Int?): SelectListAction

    data class NewList(val listName: String): SelectListAction

    data class AddToList(val animalId: Int, val listView: ListView): SelectListAction

    data class AddOrAskToList(val animalId: Int, val selectableListView: SelectableListView): SelectListAction

    data class ShowDialog(val dialogEvent: SelectListDialogEvent?): SelectListAction
}
