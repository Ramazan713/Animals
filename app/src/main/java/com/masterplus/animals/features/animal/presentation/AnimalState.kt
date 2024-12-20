package com.masterplus.animals.features.animal.presentation

import com.masterplus.animals.core.shared_features.savepoint.domain.models.SavePoint
import com.masterplus.animals.core.presentation.models.CategoryDataRowModel
import com.masterplus.animals.core.presentation.models.CategoryRowModel

data class AnimalState(
    val isLoading: Boolean = true,
    val dailyAnimals: CategoryRowModel = CategoryRowModel(),
    val savePoints: List<SavePoint> = emptyList(),
    val habitats: CategoryDataRowModel = CategoryDataRowModel(),
    val classes: CategoryDataRowModel = CategoryDataRowModel(),
    val orders: CategoryDataRowModel = CategoryDataRowModel(),
    val families: CategoryDataRowModel = CategoryDataRowModel(),
)
