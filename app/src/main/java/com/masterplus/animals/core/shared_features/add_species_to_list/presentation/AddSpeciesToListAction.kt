package com.masterplus.animals.core.shared_features.add_species_to_list.presentation

sealed interface AddSpeciesToListAction {

    data class SetListIdControl(val listIdControl: Int?): AddSpeciesToListAction

    data class ShowDialog(val dialogEvent: AddSpeciesToListDialogEvent?): AddSpeciesToListAction

    data class AddToFavorite(val speciesId: Int): AddSpeciesToListAction

    data class AddOrAskFavorite(val speciesId: Int): AddSpeciesToListAction
}