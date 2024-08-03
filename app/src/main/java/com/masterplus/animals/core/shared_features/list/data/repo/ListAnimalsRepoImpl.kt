package com.masterplus.animals.core.shared_features.list.data.repo

import com.masterplus.animals.core.shared_features.database.dao.ListAnimalsDao
import com.masterplus.animals.core.shared_features.database.dao.ListDao
import com.masterplus.animals.core.shared_features.database.entity.ListAnimalsEntity
import com.masterplus.animals.core.shared_features.database.entity.ListEntity
import com.masterplus.animals.core.shared_features.list.domain.models.ListView
import com.masterplus.animals.core.shared_features.list.domain.repo.ListAnimalsRepo
import com.masterplus.trdictionary.core.data.local.services.ListViewDao
import kotlinx.coroutines.flow.Flow

class ListAnimalsRepoImpl(
    private val listAnimalsDao: ListAnimalsDao,
    private val listDao: ListDao,
    private val listViewDao: ListViewDao
): ListAnimalsRepo {
    override fun getHasAnimalInListFlow(animalId: Int, isListFavorite: Boolean): Flow<Boolean> {
        if(isListFavorite){
            return listAnimalsDao.hasAnimalInFavoriteListFlow(animalId)
        }
        return listAnimalsDao.hasAnimalInRemovableListFlow(animalId)
    }

    override suspend fun addOrRemoveListAnimal(listView: ListView, animalId: Int) {
        val listAnimalEntity = listAnimalsDao.getListAnimalsEntity(animalId, listView.id ?: 0)
        if(listAnimalEntity != null){
            listAnimalsDao.deleteListAnimal(listAnimalEntity)
            return
        }
        val newListAnimalEntity = ListAnimalsEntity(
            listId = listView.id ?: 0,
            animalId = animalId,
            pos = listView.contentMaxPos + 1
        )
        listAnimalsDao.insertListAnimal(newListAnimalEntity)
    }

    override suspend fun addOrRemoveFavoriteAnimal(animalId: Int) {
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
            val listAnimalEntity = ListAnimalsEntity(listId = favListId.toInt(), animalId = animalId, pos = 1)
            listAnimalsDao.insertListAnimal(listAnimalEntity)
            return
        }
        val existsListAnimal = listAnimalsDao.getListAnimalsEntity(animalId = animalId, listId = favoriteList.id ?:0)
        if(existsListAnimal != null){
            listAnimalsDao.deleteListAnimal(existsListAnimal)
            return
        }
        val listAnimalEntity = ListAnimalsEntity(
            listId = favoriteList.id ?: 0,
            animalId = animalId,
            pos = favoriteList.contentMaxPos + 1
        )
        listAnimalsDao.insertListAnimal(listAnimalEntity)
    }
}