package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "ImageMetadatas",
    foreignKeys = [
        ForeignKey(
            entity = ImageEntity::class,
            parentColumns = ["id"],
            childColumns = ["image_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ImageMetadataEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val image_id: Int,
    val usage_terms: String,
    val artist_name: String?,
    val image_description: String?,
    val date_time_original: String?,
    val description_url: String,
    val license_url: String,
    val license_short_name: String
)