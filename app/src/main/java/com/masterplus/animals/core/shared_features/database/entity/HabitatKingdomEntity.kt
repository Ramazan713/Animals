package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity

@Entity(
    tableName = "HabitatKingdoms",
    primaryKeys = [
        "habitat_id", "kingdom_id"
    ]
)
data class HabitatKingdomEntity(
    val habitat_id: Int,
    val kingdom_id: Int
)
