package com.masterplus.animals.features.kingdom.data.repo

import com.masterplus.animals.core.data.mapper.toPlant
import com.masterplus.animals.core.domain.models.Plant
import com.masterplus.animals.core.shared_features.database.dao.PlantDao
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import com.masterplus.animals.features.kingdom.domain.repo.DailyPlantRepo
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.random.Random

class DailyPlantRepoImpl constructor(
    private val plantDao: PlantDao
): DailyPlantRepo {
    override suspend fun getTodayPlants(pageSize: Int, language: LanguageEnum): List<Plant> {
        val plantsResult = mutableListOf<Plant>()

        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val random = Random(today.toEpochDays())
        val plantSize = plantDao.getPlantsSize()

        for(i in 1..pageSize){
            val randomOffset = random.nextInt(plantSize)
            val plant = plantDao.getPlantByOffset(randomOffset) ?: continue
            plantsResult.add(plant.toPlant(language = language))
        }
        return plantsResult
    }

}