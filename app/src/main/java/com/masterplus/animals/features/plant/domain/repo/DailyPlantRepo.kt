package com.masterplus.animals.features.plant.domain.repo

import com.masterplus.animals.core.domain.models.Plant
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum

interface DailyPlantRepo {
    suspend fun getTodayPlants(pageSize: Int, language: LanguageEnum): List<Plant>
}