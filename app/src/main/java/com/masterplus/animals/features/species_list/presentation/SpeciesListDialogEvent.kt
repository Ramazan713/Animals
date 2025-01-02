package com.masterplus.animals.features.species_list.presentation

sealed interface SpeciesListDialogEvent {
    data class ShowEditSavePoint(
        val posIndex: Int,
        val orderKey: Int,
    ): SpeciesListDialogEvent
}