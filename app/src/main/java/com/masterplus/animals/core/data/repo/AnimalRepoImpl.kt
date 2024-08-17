package com.masterplus.animals.core.data.repo

import com.masterplus.animals.core.data.mapper.toAnimal
import com.masterplus.animals.core.data.mapper.toAnimalDetail
import com.masterplus.animals.core.domain.models.Animal
import com.masterplus.animals.core.domain.models.AnimalDetail
import com.masterplus.animals.core.domain.repo.AnimalRepo
import com.masterplus.animals.core.shared_features.database.dao.AnimalDao
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import com.masterplus.animals.core.shared_features.translation.domain.repo.TranslationRepo

class AnimalRepoImpl constructor(
    private val animalDao: AnimalDao,
): AnimalRepo {

    override suspend fun getAnimalById(animalId: Int, language: LanguageEnum): Animal? {
       return animalDao.getAnimalById(animalId)?.toAnimal(language)
    }

    override suspend fun getAnimalDetailById(animalId: Int, language: LanguageEnum): AnimalDetail? {
        return animalDao.getAnimalDetailById(animalId)?.toAnimalDetail(language)
    }

    override suspend fun getAnimalDetailBySpeciesId(speciesId: Int, language: LanguageEnum): AnimalDetail? {
        return animalDao.getAnimalDetailBySpeciesId(speciesId)?.toAnimalDetail(language)
    }
}