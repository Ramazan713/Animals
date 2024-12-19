package com.masterplus.animals.core.shared_features.backup.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class ListSpeciesBackup(
    val listId: Int,
    val speciesId: Int,
    val pos: Int
)
