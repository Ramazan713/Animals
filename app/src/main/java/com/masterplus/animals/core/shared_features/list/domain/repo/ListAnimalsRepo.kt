package com.masterplus.animals.core.shared_features.list.domain.repo

import com.masterplus.animals.core.shared_features.list.domain.models.ListView
import kotlinx.coroutines.flow.Flow

interface ListAnimalsRepo {

    fun getHasAnimalInListFlow(animalId: Int, isListFavorite: Boolean): Flow<Boolean>

    suspend fun addOrRemoveListAnimal(listView: ListView, animalId: Int)

    suspend fun addOrRemoveFavoriteAnimal(animalId: Int)

}