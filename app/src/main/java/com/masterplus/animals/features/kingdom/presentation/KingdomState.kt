package com.masterplus.animals.features.kingdom.presentation

import androidx.compose.runtime.Stable
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.presentation.models.CategoryDataRowModel
import com.masterplus.animals.core.shared_features.savepoint.domain.models.SavePoint
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum

@Stable
data class KingdomState(
    val title: UiText = UiText.Text(""),
    val isLoading: Boolean = true,
    val kingdomType: KingdomType = KingdomType.DEFAULT,
    val languageEnum: LanguageEnum = LanguageEnum.defaultValue,
    val dailySpecies: CategoryDataRowModel = CategoryDataRowModel(isLoading = false),
    val savePoints: List<SavePoint> = emptyList(),
    val isSavePointLoading: Boolean = false,
    val habitats: CategoryDataRowModel = CategoryDataRowModel(isLoading = true),
    val classes: CategoryDataRowModel = CategoryDataRowModel(isLoading = true),
    val orders: CategoryDataRowModel = CategoryDataRowModel(isLoading = true),
    val families: CategoryDataRowModel = CategoryDataRowModel(isLoading = true),
    val message: UiText? = null
)
