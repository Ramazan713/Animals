package com.masterplus.animals.core.shared_features.list.domain.repo

import com.masterplus.animals.core.shared_features.list.domain.models.ListView

interface ListAnimalsRepo {

    suspend fun addOrRemoveListAnimal(listView: ListView, animalId: Int)

    suspend fun addOrRemoveFavoriteAnimal(animalId: Int)
}