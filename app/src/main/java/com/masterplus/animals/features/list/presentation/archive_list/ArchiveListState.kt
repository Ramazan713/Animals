package com.masterplus.animals.features.list.presentation.archive_list

import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.shared_features.list.domain.models.ListView

data class ArchiveListState(
    val items: List<ListView> = emptyList(),
    val message: UiText? = null,
    val dialogEvent: ArchiveListDialogEvent? = null,
)
