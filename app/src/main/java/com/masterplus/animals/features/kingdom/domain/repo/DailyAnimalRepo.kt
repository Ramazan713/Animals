package com.masterplus.animals.features.kingdom.domain.repo

import com.masterplus.animals.core.domain.models.Animal
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum

interface DailyAnimalRepo {

    suspend fun getTodayAnimals(pageSize: Int, language: LanguageEnum): List<Animal>
}