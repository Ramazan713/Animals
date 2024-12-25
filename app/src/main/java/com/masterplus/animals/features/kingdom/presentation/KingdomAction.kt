package com.masterplus.animals.features.kingdom.presentation

import com.masterplus.animals.core.domain.enums.CategoryType

sealed interface KingdomAction {

    data object ClearMessage: KingdomAction

    data class RetryCategory(
        val categoryType: CategoryType
    ): KingdomAction
}