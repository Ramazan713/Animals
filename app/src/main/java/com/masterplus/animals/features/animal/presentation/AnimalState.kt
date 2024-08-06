package com.masterplus.animals.features.animal.presentation

import com.masterplus.animals.core.domain.models.AnimalData
import com.masterplus.animals.core.shared_features.savepoint.domain.models.SavePoint
import com.masterplus.animals.features.animal.presentation.models.CategoryRowModel

data class AnimalState(
    val isLoading: Boolean = false,
    val dailyAnimals: CategoryRowModel = CategoryRowModel(),
    val savePoints: List<SavePoint> = emptyList(),
    val habitats: CategoryRowModel = CategoryRowModel(),
    val classes: CategoryRowModel = CategoryRowModel(),
    val orders: CategoryRowModel = CategoryRowModel(),
    val families: CategoryRowModel = CategoryRowModel(),
)
