package com.masterplus.animals.core.shared_features.backup.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class HistoryBackup(
    val id: Int?,
    val content: String,
    val modifiedDateTime: String,
    val historyTypeId: Int,
    val langCode: String
)
