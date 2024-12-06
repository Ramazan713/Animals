package com.masterplus.animals.features.species_list.presentation

import com.masterplus.animals.core.domain.models.SpeciesListDetail

sealed interface SpeciesListDialogEvent {

    data class ShowEditSavePoint(
        val item: SpeciesListDetail,
        val posIndex: Int
    ): SpeciesListDialogEvent
}