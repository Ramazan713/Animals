package com.masterplus.animals.features.kingdom.data.repo

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.masterplus.animals.core.data.mapper.toSpeciesWithImages
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.ISpeciesType
import com.masterplus.animals.core.shared_features.database.dao.SpeciesDao
import com.masterplus.animals.core.shared_features.preferences.domain.AppPreferences
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import com.masterplus.animals.features.kingdom.domain.repo.DailySpeciesRepo
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.random.Random

class DailySpeciesRepoImpl(
    private val speciesDao: SpeciesDao,
    private val appPreferences: AppPreferences,
): DailySpeciesRepo {

    override suspend fun getTodaySpecies(
        kingdomType: KingdomType,
        language: LanguageEnum
    ): List<ISpeciesType> {
        val speciesIds = getDailyRandomizeSpeciesIds(kingdomType)
        val speciesList = speciesDao.getSpeciesByIds(speciesIds)

        if(speciesList.size < PAGE_SIZE) return listOf()
        return speciesList.map { it.toSpeciesWithImages(language) }
    }

    override suspend fun hasSufficientSpecies(kingdomType: KingdomType): Boolean {
        val speciesSize = speciesDao.getSpeciesCount(kingdomId = kingdomType.kingdomId)
        return hasSufficientSpecies(speciesSize)
    }

    override suspend fun checkDaily(isNewDay: Boolean){
        if(isNewDay){
            val animalSpeciesSize = speciesDao.getSpeciesCount(KingdomType.Animals.kingdomId)
            val plantSpeciesSize = speciesDao.getSpeciesCount(KingdomType.Plants.kingdomId)
            if(hasSufficientSpecies(animalSpeciesSize)){
                setDailySpecies(kingdomType = KingdomType.Animals, speciesSize = animalSpeciesSize, pageSize = PAGE_SIZE)
            }
            if(hasSufficientSpecies(plantSpeciesSize)){
                setDailySpecies(kingdomType = KingdomType.Plants, speciesSize = plantSpeciesSize, pageSize = PAGE_SIZE)
            }
        }
    }

    private suspend fun getDailyRandomizeSpeciesIds(
        kingdomType: KingdomType,
    ): List<Int> {
        val currentSpeciesSize = speciesDao.getSpeciesCount(kingdomType.kingdomId)
        val currentDaySpeciesIds = getCurrentSpeciesIds(kingdomType)

        if(currentDaySpeciesIds.size != PAGE_SIZE && hasSufficientSpecies(currentSpeciesSize)){
            setDailySpecies(
                kingdomType = kingdomType,
                speciesSize = currentSpeciesSize,
                pageSize = PAGE_SIZE
            )
            return getCurrentSpeciesIds(kingdomType)
        }
        return currentDaySpeciesIds
    }

    private suspend fun setDailySpecies(
        kingdomType: KingdomType,
        speciesSize: Int,
        pageSize: Int
    ){
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val random = Random(today.toEpochDays())

        val speciesResult = mutableSetOf<String>()
        while(speciesResult.size < pageSize){
            val randomOffset = random.nextInt(speciesSize)
            val species = speciesDao.getSpeciesByOffset(kingdomId = kingdomType.kingdomId, offset = randomOffset) ?: continue
            speciesResult.add(species.species.id.toString())
        }
        appPreferences.edit {
            it[getPreferencesKey(kingdomType)] = speciesResult
        }
    }


    private suspend fun getCurrentSpeciesIds(kingdomType: KingdomType): List<Int>{
        return appPreferences.getData()[getPreferencesKey(kingdomType)]?.mapNotNull { it.toIntOrNull() } ?: listOf()
    }

    private fun getPreferencesKey(kingdomType: KingdomType): Preferences.Key<Set<String>>{
        return stringSetPreferencesKey("DailySpeciesIds${kingdomType.name}Key")
    }

    private fun hasSufficientSpecies(
        speciesSize: Int
    ): Boolean {
        return speciesSize >= 100
    }

    companion object {
        const val PAGE_SIZE = 7
    }
}