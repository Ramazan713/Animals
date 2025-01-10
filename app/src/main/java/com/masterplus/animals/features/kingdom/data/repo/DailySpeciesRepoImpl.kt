package com.masterplus.animals.features.kingdom.data.repo

import com.masterplus.animals.core.data.mapper.toSpeciesWithImages
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.ISpeciesType
import com.masterplus.animals.core.shared_features.database.dao.SpeciesDao
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import com.masterplus.animals.features.kingdom.domain.repo.DailySpeciesRepo
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.random.Random

class DailySpeciesRepoImpl(
    private val speciesDao: SpeciesDao
): DailySpeciesRepo {

    override suspend fun getTodaySpecies(
        kingdomType: KingdomType,
        pageSize: Int,
        language: LanguageEnum
    ): List<ISpeciesType> {
        val speciesResult = mutableSetOf<ISpeciesType>()

        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val random = Random(today.toEpochDays())

        val speciesSize = speciesDao.getSpeciesCount(kingdomId = kingdomType.kingdomId)
        if(speciesSize < 100) return emptyList()

        while(speciesResult.size < pageSize){
            val randomOffset = random.nextInt(speciesSize)
            val species = speciesDao.getSpeciesByOffset(kingdomId = kingdomType.kingdomId, offset = randomOffset) ?: continue
            speciesResult.add(species.toSpeciesWithImages(language))
        }

        return speciesResult.toList()
    }
}