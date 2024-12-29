package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "remote_keys",
)
data class RemoteKeyEntity(
    @PrimaryKey(autoGenerate = false)
    val label: String,
    val nextKey: String?,
    val prevKey: String?
)