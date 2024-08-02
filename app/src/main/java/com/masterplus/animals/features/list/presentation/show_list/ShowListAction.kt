package com.masterplus.animals.features.list.presentation.show_list

import com.masterplus.animals.core.shared_features.list.domain.models.ListView

sealed interface ShowListAction{

    data class ShowDialog(
        val dialogEvent: ShowListDialogEvent? = null
    ): ShowListAction

    data class AddNewList(val listName: String): ShowListAction

    data class Rename(val listView: ListView, val newName: String): ShowListAction

    data class Copy(val listView: ListView): ShowListAction

    data class Archive(val listView: ListView): ShowListAction

    data class Delete(val listView: ListView): ShowListAction

    data object ClearMessage: ShowListAction
}
