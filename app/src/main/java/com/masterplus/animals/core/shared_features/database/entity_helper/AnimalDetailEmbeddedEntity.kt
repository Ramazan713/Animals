package com.masterplus.animals.core.shared_features.database.entity_helper

import androidx.room.Embedded
import androidx.room.Relation
import com.masterplus.animals.core.shared_features.database.entity.AnimalEntity
import com.masterplus.animals.core.shared_features.database.entity.AnimalImageEntity
import com.masterplus.animals.core.shared_features.database.entity.ClassEntity
import com.masterplus.animals.core.shared_features.database.entity.FamilyEntity
import com.masterplus.animals.core.shared_features.database.entity.GenusEntity
import com.masterplus.animals.core.shared_features.database.entity.HabitatCategoryEntity
import com.masterplus.animals.core.shared_features.database.entity.OrderEntity
import com.masterplus.animals.core.shared_features.database.entity.PhylumEntity
import com.masterplus.animals.core.shared_features.database.entity.SpeciesEntity

data class AnimalDetailEmbeddedEntity(
    @Embedded
    val animal: AnimalEntity,

    @Relation(
        parentColumn = "phylum_id",
        entityColumn = "id",
    )
    val phylum: PhylumEntity,

    @Relation(
        parentColumn = "class_id",
        entityColumn = "id",
    )
    val classEntity: ClassEntity,

    @Relation(
        parentColumn = "order_id",
        entityColumn = "id",
    )
    val order: OrderEntity,

    @Relation(
        parentColumn = "family_id",
        entityColumn = "id",
    )
    val family: FamilyEntity,

    @Relation(
        parentColumn = "genus_id",
        entityColumn = "id",
    )
    val genus: GenusEntity,

    @Relation(
        parentColumn = "species_id",
        entityColumn = "id",
    )
    val species: SpeciesEntity,

    @Relation(
        parentColumn = "habitat_category_id",
        entityColumn = "id",
    )
    val habitatCategory: HabitatCategoryEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "animal_id"
    )
    val images: List<AnimalImageEntity>,
)