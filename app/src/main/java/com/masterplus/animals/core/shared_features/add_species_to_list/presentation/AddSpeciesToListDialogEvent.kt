package com.masterplus.animals.core.shared_features.add_species_to_list.presentation

sealed interface AddSpeciesToListDialogEvent {

    data class ShowItemBottomMenu(
        val speciesId: Int,
        val orderKey: Int,
        val speciesName: String,
        val posIndex: Int
    ): AddSpeciesToListDialogEvent

    data class AskFavoriteDelete(val speciesId: Int): AddSpeciesToListDialogEvent

}