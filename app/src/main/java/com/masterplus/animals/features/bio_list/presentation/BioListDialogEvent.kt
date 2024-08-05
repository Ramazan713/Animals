package com.masterplus.animals.features.bio_list.presentation

import com.masterplus.animals.core.domain.models.AnimalData

sealed interface BioListDialogEvent {

    data class ShowItemBottomMenu(
        val item: AnimalData,
        val posIndex: Int
    ): BioListDialogEvent

    data class AskFavoriteDelete(val animalId: Int): BioListDialogEvent

    data class ShowEditSavePoint(
        val item: AnimalData,
        val posIndex: Int
    ): BioListDialogEvent
}