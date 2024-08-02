package com.masterplus.animals.features.list.presentation.archive_list

import com.masterplus.animals.core.shared_features.list.domain.models.ListView

sealed interface ArchiveListAction {

    data class ShowDialog(
        val dialogEvent: ArchiveListDialogEvent?
    ): ArchiveListAction

    data class Rename(val listView: ListView, val newName: String): ArchiveListAction

    data class UnArchive(val listView: ListView): ArchiveListAction

    data class Delete(val listView: ListView): ArchiveListAction

    data object ClearMessage: ArchiveListAction
}
