package com.masterplus.animals.features.species_detail.presentation

import com.masterplus.animals.core.domain.models.ImageWithMetadata

sealed interface SpeciesDetailDialogEvent {

    data class ShowImages(
        val images: List<ImageWithMetadata>,
        val index: Int = 0
    ): SpeciesDetailDialogEvent
}