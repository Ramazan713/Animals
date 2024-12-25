package com.masterplus.animals.features.animal.presentation

import com.masterplus.animals.core.domain.enums.CategoryType

sealed interface AnimalAction {

    data object ClearMessage: AnimalAction

    data class RetryCategory(
        val categoryType: CategoryType
    ): AnimalAction
}