package com.masterplus.animals.core.shared_features.list.domain.repo

import com.masterplus.animals.core.shared_features.list.domain.models.ListView
import kotlinx.coroutines.flow.Flow

interface ListSpeciesRepo {

    fun getHasSpeciesInListFlow(speciesId: Int, isListFavorite: Boolean): Flow<Boolean>

    suspend fun addOrRemoveListSpecies(listView: ListView, speciesId: Int)

    suspend fun addOrRemoveFavoriteSpecies(speciesId: Int)

}