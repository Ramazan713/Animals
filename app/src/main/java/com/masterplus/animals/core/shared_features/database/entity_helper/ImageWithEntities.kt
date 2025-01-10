package com.masterplus.animals.core.shared_features.database.entity_helper

import androidx.room.Embedded
import androidx.room.Relation
import com.masterplus.animals.core.domain.models.ItemOrder
import com.masterplus.animals.core.shared_features.database.entity.ClassEntity
import com.masterplus.animals.core.shared_features.database.entity.FamilyEntity
import com.masterplus.animals.core.shared_features.database.entity.HabitatCategoryEntity
import com.masterplus.animals.core.shared_features.database.entity.HabitatKingdomEntity
import com.masterplus.animals.core.shared_features.database.entity.ImageEntity
import com.masterplus.animals.core.shared_features.database.entity.OrderEntity
import com.masterplus.animals.core.shared_features.database.entity.PhylumEntity
import com.masterplus.animals.core.shared_features.database.entity.SavePointEntity

data class PhylumWithImageEmbedded(
    @Embedded
    val phylum: PhylumEntity,

    @Relation(
        entity = ImageEntity::class,
        entityColumn = "id",
        parentColumn = "image_id"
    )
    val image: ImageWithMetadataEmbedded?
): ItemOrder{
    override val orderKey: Int
        get() = phylum.order_key
}


data class ClassWithImageEmbedded(
    @Embedded
    val classEntity: ClassEntity,

    @Relation(
        entity = ImageEntity::class,
        entityColumn = "id",
        parentColumn = "image_id"
    )
    val image: ImageWithMetadataEmbedded?
): ItemOrder{
    override val orderKey: Int
        get() = classEntity.order_key
}


data class OrderWithImageEmbedded(
    @Embedded
    val order: OrderEntity,

    @Relation(
        entity = ImageEntity::class,
        entityColumn = "id",
        parentColumn = "image_id"
    )
    val image: ImageWithMetadataEmbedded?
): ItemOrder{
    override val orderKey: Int
        get() = order.order_key
}


data class FamilyWithImageEmbedded(
    @Embedded
    val family: FamilyEntity,

    @Relation(
        entity = ImageEntity::class,
        entityColumn = "id",
        parentColumn = "image_id"
    )
    val image: ImageWithMetadataEmbedded?
): ItemOrder{
    override val orderKey: Int
        get() = family.order_key
}


data class SavePointWithImageEmbedded(
    @Embedded
    val savepoint: SavePointEntity,

    @Relation(
        entity = ImageEntity::class,
        entityColumn = "id",
        parentColumn = "imageId"
    )
    val image: ImageWithMetadataEmbedded?
)


data class HabitatWithImageEmbedded(
    @Embedded
    val habitat: HabitatCategoryEntity,

    @Relation(
        entityColumn = "habitat_id",
        parentColumn = "id"
    )
    val habitatKingdoms: List<HabitatKingdomEntity>,

    @Relation(
        entity = ImageEntity::class,
        entityColumn = "id",
        parentColumn = "image_id"
    )
    val image: ImageWithMetadataEmbedded?
): ItemOrder{
    override val orderKey: Int
        get() = habitat.id
}