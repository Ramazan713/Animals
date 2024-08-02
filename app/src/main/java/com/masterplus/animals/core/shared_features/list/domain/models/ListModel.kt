package com.masterplus.animals.core.shared_features.list.domain.models

data class ListModel(
    val id: Int?,
    val name: String,
    val isRemovable: Boolean = true,
    val isArchive: Boolean = false,
    val pos: Int
)
