package com.masterplus.animals.core.shared_features.database.entity_helper

import androidx.room.Embedded
import androidx.room.Relation
import com.masterplus.animals.core.shared_features.database.entity.AnimalEntity
import com.masterplus.animals.core.shared_features.database.entity.SpeciesImageEntity
import com.masterplus.animals.core.shared_features.database.entity.SpeciesEntity

data class AnimalDataEmbedded(
    @Embedded
    val species: SpeciesEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "species_id",
    )
    val animal: AnimalEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "species_id"
    )
    val images: List<SpeciesImageEntity>,
)
