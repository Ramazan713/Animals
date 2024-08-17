package com.masterplus.animals.core.domain.repo

import com.masterplus.animals.core.domain.models.Animal
import com.masterplus.animals.core.domain.models.AnimalDetail
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum

interface AnimalRepo {

    suspend fun getAnimalById(animalId: Int, language: LanguageEnum): Animal?

    suspend fun getAnimalDetailById(animalId: Int, language: LanguageEnum): AnimalDetail?

    suspend fun getAnimalDetailBySpeciesId(speciesId: Int, language: LanguageEnum): AnimalDetail?
}