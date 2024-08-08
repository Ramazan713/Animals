package com.masterplus.animals.core.shared_features.database.entity_helper

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.masterplus.animals.core.shared_features.database.entity.ListSpeciesEntity
import com.masterplus.animals.core.shared_features.database.entity.ListEntity
import com.masterplus.animals.core.shared_features.database.entity.SpeciesEntity
import com.masterplus.animals.core.shared_features.database.entity.SpeciesImageEntity

data class SpeciesDetailEmbedded(
    @Embedded
    val species: SpeciesEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "species_id"
    )
    val images: List<SpeciesImageEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = ListSpeciesEntity::class,
            parentColumn = "speciesId",
            entityColumn = "listId"
        ),
        projection = ["isRemovable"],
        entity = ListEntity::class
    )
    val listsIsRemovable: List<Boolean>
)
