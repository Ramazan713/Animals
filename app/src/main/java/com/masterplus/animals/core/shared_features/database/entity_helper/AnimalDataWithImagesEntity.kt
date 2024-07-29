package com.masterplus.animals.core.shared_features.database.entity_helper

import androidx.room.Embedded
import androidx.room.Relation
import com.masterplus.animals.core.shared_features.database.entity.AnimalEntity
import com.masterplus.animals.core.shared_features.database.entity.AnimalImageEntity
import com.masterplus.animals.core.shared_features.database.entity.SpeciesEntity

data class AnimalDataWithImagesEntity(
    @Embedded
    val animalData: AnimalDataEntityHelper,
    @Relation(
        parentColumn = "id",
        entityColumn = "animal_id"
    )
    val images: List<AnimalImageEntity>,

    @Relation(
        parentColumn = "species_id",
        entityColumn = "id",
    )
    val species: SpeciesEntity
)
