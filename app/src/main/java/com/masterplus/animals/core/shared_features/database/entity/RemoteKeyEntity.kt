package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "RemoteKeys",
)
data class RemoteKeyEntity(
    @PrimaryKey(autoGenerate = false)
    val label: String,
    val next_key: String?,
    val prev_key: String?,
    val is_next_key_end: Boolean = false,
    val should_refresh: Boolean = false
)