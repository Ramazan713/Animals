package com.masterplus.animals.core.shared_features.database.entity_helper

import androidx.room.Embedded
import androidx.room.Relation
import com.masterplus.animals.core.shared_features.database.entity.ImageEntity
import com.masterplus.animals.core.shared_features.database.entity.ImageMetadataEntity

data class ImageWithMetadataEmbedded(
    @Embedded
    val image: ImageEntity,

    @Relation(
        entity = ImageMetadataEntity::class,
        parentColumn = "id",
        entityColumn = "image_id"
    )
    val metadata: ImageMetadataEntity?
)