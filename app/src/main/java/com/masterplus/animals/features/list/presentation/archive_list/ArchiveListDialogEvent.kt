package com.masterplus.animals.features.list.presentation.archive_list

import com.masterplus.animals.core.shared_features.list.domain.models.ListView
import com.masterplus.animals.features.list.domain.enums.ArchiveListBottomMenuEnum

sealed interface ArchiveListDialogEvent{

    data class ShowListBottomMenu(
        val items: List<ArchiveListBottomMenuEnum>,
        val title: String?,
        val listView: ListView,
    ): ArchiveListDialogEvent

    data class AskDelete(val listView: ListView): ArchiveListDialogEvent

    data class Rename(val listView: ListView): ArchiveListDialogEvent

    data class AskUnArchive(val listView: ListView): ArchiveListDialogEvent
}
