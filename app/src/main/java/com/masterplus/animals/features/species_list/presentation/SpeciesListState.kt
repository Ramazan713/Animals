package com.masterplus.animals.features.species_list.presentation

import com.masterplus.animals.core.domain.utils.UiText

data class SpeciesListState(
    val title: UiText = UiText.Text("Hayvanlar Listesi"),
    val dialogEvent: SpeciesListDialogEvent? = null,
    val listIdControl: Int? = null
)
