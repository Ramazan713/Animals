package com.masterplus.animals.features.species_detail.presentation

sealed interface SpeciesDetailDialogEvent {

    data class ShowImages(
        val imageUrls: List<String>,
        val index: Int = 0
    ): SpeciesDetailDialogEvent
}