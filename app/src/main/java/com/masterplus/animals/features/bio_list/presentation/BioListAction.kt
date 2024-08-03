package com.masterplus.animals.features.bio_list.presentation

import com.masterplus.animals.core.domain.models.AnimalData

sealed interface BioListAction {

    data class ShowDialog(val dialogEvent: BioListDialogEvent?): BioListAction

    data class FavoriteItem(val animalData: AnimalData): BioListAction
}