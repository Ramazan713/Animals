package com.masterplus.animals.core.domain.models


data class ImageWithMetadata(
    val id: Int?,
    val imagePath: String,
    val imageUrl: String,
    val metadata: ImageMetadata?
)

data class ImageData(
    val id: Int?,
    val imagePath: String,
    val imageUrl: String
)


data class ImageMetadata(
    val id: Int?,
    val imageId: Int,
    val usageTerms: String,
    val artistName: String?,
    val imageDescription: String?,
    val dateTimeOriginal: String?,
    val descriptionUrl: String,
    val licenseUrl: String,
    val licenseShortName: String
)