package com.masterplus.animals.core.shared_features.database.entity_helper

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.masterplus.animals.core.shared_features.database.entity.AnimalImageEntity
import com.masterplus.animals.core.shared_features.database.entity.ListAnimalsEntity
import com.masterplus.animals.core.shared_features.database.entity.ListEntity
import com.masterplus.animals.core.shared_features.database.entity.SpeciesEntity

data class AnimalDataDetailEmbedded(
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
    val species: SpeciesEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = ListAnimalsEntity::class,
            parentColumn = "animalId",
            entityColumn = "listId"
        ),
        projection = ["isRemovable"],
        entity = ListEntity::class
    )
    val listsIsRemovable: List<Boolean>
)
