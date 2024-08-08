package com.masterplus.animals.core.shared_features.list.data.repo

import com.masterplus.animals.core.shared_features.database.dao.ListSpeciesDao
import com.masterplus.animals.core.shared_features.database.dao.ListDao
import com.masterplus.animals.core.shared_features.database.entity.ListEntity
import com.masterplus.animals.core.shared_features.database.entity.ListSpeciesEntity
import com.masterplus.animals.core.shared_features.list.domain.models.ListView
import com.masterplus.animals.core.shared_features.list.domain.repo.ListSpeciesRepo
import com.masterplus.trdictionary.core.data.local.services.ListViewDao
import kotlinx.coroutines.flow.Flow

class ListSpeciesRepoImpl(
    private val listSpeciesDao: ListSpeciesDao,
    private val listDao: ListDao,
    private val listViewDao: ListViewDao
): ListSpeciesRepo {
    override fun getHasSpeciesInListFlow(speciesId: Int, isListFavorite: Boolean): Flow<Boolean> {
        if(isListFavorite){
            return listSpeciesDao.hasSpeciesInFavoriteListFlow(speciesId)
        }
        return listSpeciesDao.hasSpeciesInRemovableListFlow(speciesId)
    }

    override suspend fun addOrRemoveListSpecies(listView: ListView, speciesId: Int) {
        val listSpeciesEntity = listSpeciesDao.getListSpecies(speciesId, listView.id ?: 0)
        if(listSpeciesEntity != null){
            listSpeciesDao.deleteListSpecies(listSpeciesEntity)
            return
        }
        val newListSpeciesEntity = ListSpeciesEntity(
            listId = listView.id ?: 0,
            speciesId = speciesId,
            pos = listView.contentMaxPos + 1
        )
        listSpeciesDao.insertListSpecies(newListSpeciesEntity)
    }

    override suspend fun addOrRemoveFavoriteSpecies(speciesId: Int) {
        val favoriteList = listViewDao.getFavoriteList()
        if(favoriteList == null){
           val favListId = listDao.insertList(
               ListEntity(
                   id = null,
                   name = "Favoriler",
                   isRemovable = false,
                   isArchive = false,
                   pos = 1
               )
           )
            val listSpeciesEntity = ListSpeciesEntity(listId = favListId.toInt(), speciesId = speciesId, pos = 1)
            listSpeciesDao.insertListSpecies(listSpeciesEntity)
            return
        }
        val existsListSpecies = listSpeciesDao.getListSpecies(speciesId = speciesId, listId = favoriteList.id ?:0)
        if(existsListSpecies != null){
            listSpeciesDao.deleteListSpecies(existsListSpecies)
            return
        }
        val listSpeciesEntity = ListSpeciesEntity(
            listId = favoriteList.id ?: 0,
            speciesId = speciesId,
            pos = favoriteList.contentMaxPos + 1
        )
        listSpeciesDao.insertListSpecies(listSpeciesEntity)
    }
}