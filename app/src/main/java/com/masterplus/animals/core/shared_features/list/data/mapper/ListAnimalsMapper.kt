package com.masterplus.animals.core.shared_features.list.data.mapper

import com.masterplus.animals.core.shared_features.database.entity.ListSpeciesEntity
import com.masterplus.animals.core.shared_features.list.domain.models.ListSpecies

fun ListSpeciesEntity.toListSpecies(): ListSpecies {
    return ListSpecies(
        listId = listId,
        speciesId = speciesId,
        pos = pos
    )
}

fun ListSpecies.toListSpeciesEntity(): ListSpeciesEntity {
    return ListSpeciesEntity(
        listId = listId,
        speciesId = speciesId,
        pos = pos
    )
}
