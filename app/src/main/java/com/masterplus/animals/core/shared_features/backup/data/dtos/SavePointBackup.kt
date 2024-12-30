package com.masterplus.animals.core.shared_features.backup.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class SavePointBackup(
    val id: Int?,
    val titleEn: String,
    val titleTr: String,
    val contentTypeId: Int,
    val destinationTypeId: Int,
    val destinationId: Int?,
    val kingdomId: Int,
    val saveModeId: Int,
    val saveKey: String?,
    val itemId: Int,
    val modifiedTime: String,
    val imageId: Int?,
)
