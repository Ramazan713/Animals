package com.masterplus.animals.features.animal.presentation

import com.masterplus.animals.core.presentation.models.ImageWithTitleModel

data class AnimalState(
    val isLoading: Boolean = false,
    val habitats: List<ImageWithTitleModel> = emptyList(),
    val classes: List<ImageWithTitleModel> = emptyList(),
    val orders: List<ImageWithTitleModel> = emptyList(),
    val families: List<ImageWithTitleModel> = emptyList()
)
