package com.masterplus.animals.features.bio_list.presentation

sealed interface BioListAction {

    data class ShowDialog(val dialogEvent: BioListDialogEvent?): BioListAction

    data class AddToFavorite(val animalId: Int): BioListAction

    data class AddOrAskFavorite(val animalId: Int): BioListAction
}