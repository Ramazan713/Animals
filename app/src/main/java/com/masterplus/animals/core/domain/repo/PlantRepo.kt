package com.masterplus.animals.core.domain.repo

import com.masterplus.animals.core.domain.models.PlantDetail
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum


interface PlantRepo {

    suspend fun getPlantDetailBySpeciesId(speciesId: Int, language: LanguageEnum): PlantDetail?
}