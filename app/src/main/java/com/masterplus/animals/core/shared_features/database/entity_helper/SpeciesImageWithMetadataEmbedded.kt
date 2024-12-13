package com.masterplus.animals.core.shared_features.database.entity_helper

import androidx.room.Embedded
import androidx.room.Relation
import com.masterplus.animals.core.shared_features.database.entity.ImageEntity
import com.masterplus.animals.core.shared_features.database.entity.SpeciesImageEntity

data class SpeciesImageWithMetadataEmbedded(
    @Embedded
    val speciesImage: SpeciesImageEntity,

    @Relation(
        parentColumn = "image_id",
        entityColumn = "id",
        entity = ImageEntity::class
    )
    val imageWithMetadata: ImageWithMetadataEmbedded
)

//
//data class SpeciesImageWithMetadataEmbedded(
//    @Embedded
//    val speciesImage: SpeciesImageEntity,
//
//    @Relation(
//        entity = ImageEntity::class,
//        parentColumn = "image_id",
//        entityColumn = "id"
//    )
//    val image: ImageEntity,
//
//    @Relation(
//        entity = ImageMetadataEntity::class,
//        parentColumn = "image_id",
//        entityColumn = "image_id"
//    )
//    val metadata: ImageMetadataEntity?
//)