package com.masterplus.animals.core.shared_features.database.entity

import androidx.room.Entity
import androidx.room.Index


@Entity(
    tableName = "ListSpecies",
    primaryKeys = ["listId","speciesId"],
    indices = [
        Index("speciesId")
    ],
)
data class ListSpeciesEntity(
    val listId: Int,
    val speciesId: Int,
    val pos: Int
)
