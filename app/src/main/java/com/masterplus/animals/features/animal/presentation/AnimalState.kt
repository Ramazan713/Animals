package com.masterplus.animals.features.animal.presentation

import com.masterplus.animals.core.domain.models.ClassModel
import com.masterplus.animals.core.domain.models.FamilyModel
import com.masterplus.animals.core.domain.models.HabitatCategoryModel
import com.masterplus.animals.core.domain.models.OrderModel

data class AnimalState(
    val isLoading: Boolean = false,
    val habitats: List<HabitatCategoryModel> = emptyList(),
    val classes: List<ClassModel> = emptyList(),
    val orders: List<OrderModel> = emptyList(),
    val families: List<FamilyModel> = emptyList()
)
