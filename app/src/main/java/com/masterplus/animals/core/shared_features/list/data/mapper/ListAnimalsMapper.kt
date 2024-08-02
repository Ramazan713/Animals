package com.masterplus.animals.core.shared_features.list.data.mapper

import com.masterplus.animals.core.shared_features.database.entity.ListAnimalsEntity
import com.masterplus.animals.core.shared_features.list.domain.models.ListAnimals

fun ListAnimalsEntity.toListAnimals(): ListAnimals {
    return ListAnimals(
        listId = listId,
        animalId = animalId,
        pos = pos
    )
}

fun ListAnimals.toListWordsEntity(): ListAnimalsEntity {
    return ListAnimalsEntity(
        listId = listId,
        animalId = animalId,
        pos = pos
    )
}
