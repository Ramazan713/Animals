package com.masterplus.animals.core.shared_features.database.entity_helper

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.masterplus.animals.core.shared_features.database.entity.ClassEntity
import com.masterplus.animals.core.shared_features.database.entity.FamilyEntity
import com.masterplus.animals.core.shared_features.database.entity.GenusEntity
import com.masterplus.animals.core.shared_features.database.entity.HabitatCategoryEntity
import com.masterplus.animals.core.shared_features.database.entity.ListEntity
import com.masterplus.animals.core.shared_features.database.entity.ListSpeciesEntity
import com.masterplus.animals.core.shared_features.database.entity.OrderEntity
import com.masterplus.animals.core.shared_features.database.entity.PhylumEntity
import com.masterplus.animals.core.shared_features.database.entity.PlantEntity
import com.masterplus.animals.core.shared_features.database.entity.SpeciesEntity
import com.masterplus.animals.core.shared_features.database.entity.SpeciesHabitatCategoryEntity
import com.masterplus.animals.core.shared_features.database.entity.SpeciesImageEntity
import com.masterplus.animals.core.shared_features.database.view.SpeciesRelationsView

data class PlantDetailEmbedded(
    @Embedded
    val species: SpeciesEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "species_id",
    )
    val plant: PlantEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = SpeciesRelationsView::class,
            parentColumn = "species_id",
            entityColumn = "phylum_id"
        )
    )
    val phylum: PhylumEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = SpeciesRelationsView::class,
            parentColumn = "species_id",
            entityColumn = "class_id"
        )
    )
    val classEntity: ClassEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = SpeciesRelationsView::class,
            parentColumn = "species_id",
            entityColumn = "order_id"
        )
    )
    val order: OrderEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = SpeciesRelationsView::class,
            parentColumn = "species_id",
            entityColumn = "family_id"
        )
    )
    val family: FamilyEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = SpeciesRelationsView::class,
            parentColumn = "species_id",
            entityColumn = "genuse_id"
        )
    )
    val genus: GenusEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = SpeciesHabitatCategoryEntity::class,
            entityColumn = "category_id",
            parentColumn = "species_id"
        ),
        entity = HabitatCategoryEntity::class
    )
    val habitatCategories: List<HabitatCategoryEntity>,

    @Relation(
        parentColumn = "id",
        entityColumn = "species_id",
        entity = SpeciesImageEntity::class
    )
    val images: List<SpeciesImageWithMetadataEmbedded>,

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