package com.masterplus.animals.core.data.repo

import com.masterplus.animals.core.data.mapper.toAnimalDetail
import com.masterplus.animals.core.domain.models.AnimalDetail
import com.masterplus.animals.core.domain.repo.AnimalRepo
import com.masterplus.animals.core.shared_features.database.dao.AnimalDao
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum

class AnimalRepoImpl constructor(
    private val animalDao: AnimalDao,
): AnimalRepo {

    override suspend fun getAnimalDetailBySpeciesId(speciesId: Int, language: LanguageEnum): AnimalDetail? {
        return animalDao.getAnimalDetailBySpeciesId(speciesId)?.toAnimalDetail(language)
    }
}