package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "SavePoints"
)
data class SavePointEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val titleEn: String,
    val titleTr: String,
    val contentTypeId: Int,
    val destinationTypeId: Int,
    val destinationId: Int?,
    val saveKey: String?,
    val itemPosIndex: Int,
    val modifiedTime: String,
    val imagePath: String?,
    val imageUrl: String?
)
