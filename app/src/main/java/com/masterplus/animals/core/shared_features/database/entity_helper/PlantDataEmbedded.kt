package com.masterplus.animals.core.shared_features.database.entity_helper

import androidx.room.Embedded
import androidx.room.Relation
import com.masterplus.animals.core.shared_features.database.entity.PlantEntity
import com.masterplus.animals.core.shared_features.database.entity.SpeciesEntity
import com.masterplus.animals.core.shared_features.database.entity.SpeciesImageEntity

data class PlantDataEmbedded(
    @Embedded
    val species: SpeciesEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "species_id",
    )
    val plant: PlantEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "species_id",
        entity = SpeciesImageEntity::class
    )
    val images: List<SpeciesImageWithMetadataEmbedded>,
)
