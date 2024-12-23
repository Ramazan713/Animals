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
