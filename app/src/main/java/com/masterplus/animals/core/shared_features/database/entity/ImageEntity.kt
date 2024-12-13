package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Images")
data class ImageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val image_path: String,
    val image_url: String,
    val created_at: String,
    val updated_at: String
)