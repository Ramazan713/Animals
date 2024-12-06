package com.masterplus.animals.core.shared_features.add_species_to_list.presentation

import com.masterplus.animals.core.domain.models.SpeciesListDetail

sealed interface AddSpeciesToListDialogEvent {

    data class ShowItemBottomMenu(
        val item: SpeciesListDetail,
        val posIndex: Int
    ): AddSpeciesToListDialogEvent

    data class AskFavoriteDelete(val speciesId: Int): AddSpeciesToListDialogEvent

}