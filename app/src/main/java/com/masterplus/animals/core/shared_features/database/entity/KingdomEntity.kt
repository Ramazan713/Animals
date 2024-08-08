package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "Kingdoms"
)
data class KingdomEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val name: String
)
