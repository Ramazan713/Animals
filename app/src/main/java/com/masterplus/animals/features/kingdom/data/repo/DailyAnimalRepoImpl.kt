package com.masterplus.animals.features.kingdom.data.repo

import com.masterplus.animals.core.data.mapper.toAnimal
import com.masterplus.animals.core.domain.models.Animal
import com.masterplus.animals.core.shared_features.database.dao.AnimalDao
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import com.masterplus.animals.features.kingdom.domain.repo.DailyAnimalRepo
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.random.Random

class DailyAnimalRepoImpl(
    private val animalDao: AnimalDao
): DailyAnimalRepo {

    override suspend fun getTodayAnimals(pageSize: Int, language: LanguageEnum): List<Animal> {
        val animalsResult = mutableListOf<Animal>()

        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val random = Random(today.toEpochDays())
        val animalSize = animalDao.getAnimalsSize()

        for(i in 1..pageSize){
            val randomOffset = random.nextInt(animalSize)
            val animal = animalDao.getAnimalByOffset(randomOffset) ?: continue
            animalsResult.add(animal.toAnimal(language = language))
        }
        return animalsResult
    }
}