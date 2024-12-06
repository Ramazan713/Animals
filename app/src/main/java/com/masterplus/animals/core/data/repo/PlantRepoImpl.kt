package com.masterplus.animals.core.data.repo

import com.masterplus.animals.core.data.mapper.toPlant
import com.masterplus.animals.core.data.mapper.toPlantDetail
import com.masterplus.animals.core.domain.models.Plant
import com.masterplus.animals.core.domain.models.PlantDetail
import com.masterplus.animals.core.domain.repo.PlantRepo
import com.masterplus.animals.core.shared_features.database.dao.PlantDao
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum

class PlantRepoImpl(
    private val plantDao: PlantDao
): PlantRepo {
    override suspend fun getPlantById(plantId: Int, language: LanguageEnum): Plant? {
        return plantDao.getPlantById(plantId)?.toPlant(language)
    }

    override suspend fun getPlantDetailById(plantId: Int, language: LanguageEnum): PlantDetail? {
        return plantDao.getPlantDetailById(plantId)?.toPlantDetail(language)
    }

    override suspend fun getPlantDetailBySpeciesId(speciesId: Int, language: LanguageEnum): PlantDetail? {
        return plantDao.getPlantDetailBySpeciesId(speciesId)?.toPlantDetail(language)
    }
}