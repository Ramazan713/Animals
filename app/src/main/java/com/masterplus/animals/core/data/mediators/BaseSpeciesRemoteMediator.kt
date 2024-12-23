package com.masterplus.animals.core.data.mediators

import com.masterplus.animals.core.data.dtos.SpeciesDto
import com.masterplus.animals.core.data.mapper.toAnimalEntity
import com.masterplus.animals.core.data.mapper.toPlantEntity
import com.masterplus.animals.core.data.mapper.toSpeciesEntity
import com.masterplus.animals.core.data.mapper.toSpeciesHabitatCategoryEntity
import com.masterplus.animals.core.data.mapper.toSpeciesImageWithMetadataEmbedded
import com.masterplus.animals.core.shared_features.database.AppDatabase

abstract class BaseSpeciesRemoteMediator<T: Any>(
    override val db: AppDatabase,
): BaseRemoteMediator2<T, SpeciesDto>(db) {

    override suspend fun insertData(items: List<SpeciesDto>) {
        val species = items.map { it.toSpeciesEntity() }
        val speciesHabitats = items.flatMap { it.toSpeciesHabitatCategoryEntity() }
        val animals = items.mapNotNull { it.animalia?.toAnimalEntity(it.id) }
        val plants = items.mapNotNull { it.plantae?.toPlantEntity(it.id) }
        val speciesImageWithMetadata = items.flatMap { it.toSpeciesImageWithMetadataEmbedded() }
        val images = speciesImageWithMetadata.map { it.imageWithMetadata.image }
        val imageMetadatas = speciesImageWithMetadata.mapNotNull { it.imageWithMetadata.metadata }
        val speciesImages = speciesImageWithMetadata.map { it.speciesImage }

        db.categoryDao.insertImages(images)
        db.categoryDao.insertMetadatas(imageMetadatas)
        db.speciesDao.insertSpecies(species)
        db.speciesDao.insertSpeciesHabitats(speciesHabitats)
        db.animalDao.insertAnimals(animals)
        db.plantDao.insertPlants(plants)
        db.speciesDao.insertSpeciesImages(speciesImages)
    }

    override fun getNextKey(items: List<SpeciesDto>): Int? {
        return items.lastOrNull()?.id
    }
}