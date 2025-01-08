package com.masterplus.animals.core.data.helpers

import com.masterplus.animals.core.data.dtos.SpeciesDto
import com.masterplus.animals.core.data.mapper.toAnimalEntity
import com.masterplus.animals.core.data.mapper.toPlantEntity
import com.masterplus.animals.core.data.mapper.toSpeciesEntity
import com.masterplus.animals.core.data.mapper.toSpeciesHabitatCategoryEntity
import com.masterplus.animals.core.data.mapper.toSpeciesImageWithMetadataEmbedded
import com.masterplus.animals.core.shared_features.database.AppDatabase

class InsertFirebaseSpeciesHelper(
    private val db: AppDatabase
) {
    
    suspend fun insertSpecies(speciesList: List<SpeciesDto>, label: String){
        val species = speciesList.map { it.toSpeciesEntity(label) }
        val speciesHabitats = speciesList.flatMap { it.toSpeciesHabitatCategoryEntity() }
        val animals = speciesList.mapNotNull { it.animalia?.toAnimalEntity(it.id, label) }
        val plants = speciesList.mapNotNull { it.plantae?.toPlantEntity(it.id, label) }
        val speciesImageWithMetadata = speciesList.flatMap { it.toSpeciesImageWithMetadataEmbedded(label) }
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
}