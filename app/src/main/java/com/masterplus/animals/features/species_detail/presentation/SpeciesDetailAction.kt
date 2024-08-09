package com.masterplus.animals.features.species_detail.presentation

import com.masterplus.animals.features.species_detail.domain.enums.SpeciesInfoPageEnum

sealed interface SpeciesDetailAction {

    data class ChangePage(val page: SpeciesInfoPageEnum): SpeciesDetailAction

    data class ShowDialog(val dialogEvent: SpeciesDetailDialogEvent?): SpeciesDetailAction
}