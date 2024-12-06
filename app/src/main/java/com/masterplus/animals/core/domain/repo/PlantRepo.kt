package com.masterplus.animals.core.domain.repo

import com.masterplus.animals.core.domain.models.Plant
import com.masterplus.animals.core.domain.models.PlantDetail
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum


interface PlantRepo {

    suspend fun getPlantById(plantId: Int, language: LanguageEnum): Plant?

    suspend fun getPlantDetailById(plantId: Int, language: LanguageEnum): PlantDetail?

    suspend fun getPlantDetailBySpeciesId(speciesId: Int, language: LanguageEnum): PlantDetail?
}