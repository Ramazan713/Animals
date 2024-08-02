package com.masterplus.animals.features.list.presentation.show_list

import com.masterplus.animals.core.shared_features.list.domain.models.ListView
import com.masterplus.animals.features.list.domain.enums.ShowListBottomMenuEnum

sealed interface ShowListDialogEvent{

    data class ShowListBottomMenu(
        val items: List<ShowListBottomMenuEnum>,
        val title: String?,
        val listView: ListView,
    ): ShowListDialogEvent

    data class AskDelete(val listView: ListView): ShowListDialogEvent

    data object TitleToAddList: ShowListDialogEvent

    data class Rename(val listView: ListView): ShowListDialogEvent

    data class AskArchive(val listView: ListView): ShowListDialogEvent

    data class AskCopy(val listView: ListView): ShowListDialogEvent
}
