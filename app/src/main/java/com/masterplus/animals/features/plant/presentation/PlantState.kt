package com.masterplus.animals.features.plant.presentation

import com.masterplus.animals.core.presentation.models.CategoryDataRowModel
import com.masterplus.animals.core.presentation.models.CategoryRowModel
import com.masterplus.animals.core.shared_features.savepoint.domain.models.SavePoint


data class PlantState(
    val isLoading: Boolean = false,
    val dailyPlants: CategoryRowModel = CategoryRowModel(),
    val savePoints: List<SavePoint> = emptyList(),
    val habitats: CategoryDataRowModel = CategoryDataRowModel(),
    val classes: CategoryDataRowModel = CategoryDataRowModel(),
    val orders: CategoryDataRowModel = CategoryDataRowModel(),
    val families: CategoryDataRowModel = CategoryDataRowModel(),
)
