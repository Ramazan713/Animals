package com.masterplus.animals.core.domain.repo

import com.masterplus.animals.core.domain.models.Animal
import com.masterplus.animals.core.domain.models.AnimalDetail

interface AnimalRepo {

    suspend fun getAnimalById(animalId: Int): Animal?

    suspend fun getAnimalDetailById(animalId: Int): AnimalDetail?

}