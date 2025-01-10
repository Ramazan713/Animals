package com.masterplus.animals.features.kingdom.domain.repo

import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.ISpeciesType
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum

interface DailySpeciesRepo {

    suspend fun getTodaySpecies(
        kingdomType: KingdomType,
        pageSize: Int,
        language: LanguageEnum
    ): List<ISpeciesType>
}