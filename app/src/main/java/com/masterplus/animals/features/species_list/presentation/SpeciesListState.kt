package com.masterplus.animals.features.species_list.presentation

import com.masterplus.animals.core.domain.utils.UiText

data class SpeciesListState(
    val label: String = "",
    val title: UiText? = null,
    val dialogEvent: SpeciesListDialogEvent? = null,
    val listIdControl: Int? = null,
)
