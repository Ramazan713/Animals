package com.masterplus.animals.core.shared_features.list.presentation.select_list

import com.masterplus.animals.core.shared_features.list.domain.models.ListView

sealed interface SelectListDialogEvent{

    data class AskListDelete(val animalId: Int, val listView: ListView): SelectListDialogEvent
}
