package com.masterplus.animals.features.bio_detail.presentation

sealed interface BioDetailDialogEvent {

    data class ShowImages(
        val imageUrls: List<String>,
        val index: Int = 0
    ): BioDetailDialogEvent
}