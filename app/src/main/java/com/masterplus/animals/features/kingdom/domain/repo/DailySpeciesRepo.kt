package com.masterplus.animals.features.kingdom.domain.repo

import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.ISpeciesType
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum

interface DailySpeciesRepo {

    suspend fun getTodaySpecies(
        kingdomType: KingdomType,
        language: LanguageEnum
    ): List<ISpeciesType>

    suspend fun hasSufficientSpecies(
        kingdomType: KingdomType
    ): Boolean

    suspend fun checkDaily(isNewDay: Boolean)
}