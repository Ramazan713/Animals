package com.masterplus.animals.features.animal.domain.repo

import com.masterplus.animals.core.domain.models.AnimalData

interface DailyAnimalRepo {

    suspend fun getTodayAnimals(pageSize: Int): List<AnimalData>
}