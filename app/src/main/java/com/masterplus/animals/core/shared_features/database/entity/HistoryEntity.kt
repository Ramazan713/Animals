package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "Histories"
)
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val content: String,
    val lang_code: String,
    val modified_date_time: String,
    val history_type_id: Int
)
