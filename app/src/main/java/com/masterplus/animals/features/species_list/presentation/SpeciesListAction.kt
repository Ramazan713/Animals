package com.masterplus.animals.features.species_list.presentation

sealed interface SpeciesListAction {

    data class ShowDialog(val dialogEvent: SpeciesListDialogEvent?): SpeciesListAction

    data class SavePosition(val posIndex: Int): SpeciesListAction
}