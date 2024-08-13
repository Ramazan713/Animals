package com.masterplus.animals.core.data.repo

import com.masterplus.animals.core.data.mapper.toAnimal
import com.masterplus.animals.core.data.mapper.toAnimalDetail
import com.masterplus.animals.core.domain.models.Animal
import com.masterplus.animals.core.domain.models.AnimalDetail
import com.masterplus.animals.core.domain.repo.AnimalRepo
import com.masterplus.animals.core.shared_features.database.dao.AnimalDao

class AnimalRepoImpl constructor(
    private val animalDao: AnimalDao
): AnimalRepo {

    override suspend fun getAnimalById(animalId: Int): Animal? {
       return animalDao.getAnimalById(animalId)?.toAnimal()
    }

    override suspend fun getAnimalDetailById(animalId: Int): AnimalDetail? {
        return animalDao.getAnimalDetailById(animalId)?.toAnimalDetail()
    }

    override suspend fun getAnimalDetailBySpeciesId(speciesId: Int): AnimalDetail? {
        return animalDao.getAnimalDetailBySpeciesId(speciesId)?.toAnimalDetail()
    }

}