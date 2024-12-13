package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.models.ImageData
import com.masterplus.animals.core.domain.models.ImageMetadata
import com.masterplus.animals.core.domain.models.ImageWithMetadata
import com.masterplus.animals.core.domain.models.SpeciesImageModel
import com.masterplus.animals.core.shared_features.database.entity.ImageEntity
import com.masterplus.animals.core.shared_features.database.entity.ImageMetadataEntity
import com.masterplus.animals.core.shared_features.database.entity_helper.ImageWithMetadataEmbedded
import com.masterplus.animals.core.shared_features.database.entity_helper.SpeciesImageWithMetadataEmbedded


fun SpeciesImageWithMetadataEmbedded.toSpeciesImageModel(): SpeciesImageModel{
    return with(speciesImage){
        SpeciesImageModel(
            speciesId = species_id,
            imageOrder = image_order,
            image = imageWithMetadata.toImageWithMetadata()
        )
    }
}


fun ImageWithMetadataEmbedded.toImageWithMetadata(): ImageWithMetadata {
    return with(image){
        ImageWithMetadata(
            id = id,
            imagePath = image_path,
            imageUrl = image_url,
            metadata = metadata?.toImageMetadata()
        )
    }
}

fun ImageEntity.toImageData(): ImageData {
    return ImageData(
        id = this.id,
        imagePath = this.image_path,
        imageUrl = this.image_url,
        createdAt = this.created_at,
        updatedAt = this.updated_at
    )
}

fun ImageMetadataEntity.toImageMetadata(): ImageMetadata {
    return ImageMetadata(
        id = this.id,
        imageId = this.image_id,
        usageTerms = this.usage_terms,
        artistName = this.artist_name,
        imageDescription = this.image_description,
        dateTimeOriginal = this.date_time_original,
        descriptionUrl = this.description_url,
        licenseUrl = this.license_url,
        licenseShortName = this.license_short_name
    )
}