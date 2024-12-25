package com.masterplus.animals.features.animal.presentation

import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.shared_features.savepoint.domain.models.SavePoint
import com.masterplus.animals.core.presentation.models.CategoryDataRowModel
import com.masterplus.animals.core.presentation.models.CategoryRowModel
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum

data class AnimalState(
    val isLoading: Boolean = true,
    val languageEnum: LanguageEnum = LanguageEnum.defaultValue,
    val dailyAnimals: CategoryRowModel = CategoryRowModel(isLoading = true),
    val savePoints: List<SavePoint> = emptyList(),
    val isSavePointLoading: Boolean = false,
    val habitats: CategoryDataRowModel = CategoryDataRowModel(isLoading = true),
    val classes: CategoryDataRowModel = CategoryDataRowModel(isLoading = true),
    val orders: CategoryDataRowModel = CategoryDataRowModel(isLoading = true),
    val families: CategoryDataRowModel = CategoryDataRowModel(isLoading = true),
    val message: UiText? = null
)
