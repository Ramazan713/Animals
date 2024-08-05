package com.masterplus.animals.features.bio_list.presentation

import com.masterplus.animals.core.domain.utils.UiText

data class BioListState(
    val title: UiText = UiText.Text("Hayvanlar Listesi"),
    val dialogEvent: BioListDialogEvent? = null,
    val listIdControl: Int? = null
)
