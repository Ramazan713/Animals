package com.masterplus.animals.features.species_list.presentation

import com.masterplus.animals.core.domain.models.SpeciesDetail

sealed interface SpeciesListDialogEvent {

    data class ShowEditSavePoint(
        val item: SpeciesDetail,
        val posIndex: Int
    ): SpeciesListDialogEvent
}