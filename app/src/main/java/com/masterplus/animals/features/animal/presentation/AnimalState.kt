package com.masterplus.animals.features.animal.presentation

import com.masterplus.animals.features.animal.presentation.models.CategoryRowModel

data class AnimalState(
    val isLoading: Boolean = false,
    val habitats: CategoryRowModel = CategoryRowModel(),
    val classes: CategoryRowModel = CategoryRowModel(),
    val orders: CategoryRowModel = CategoryRowModel(),
    val families: CategoryRowModel = CategoryRowModel(),
)
