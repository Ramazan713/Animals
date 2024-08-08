package com.masterplus.animals.core.shared_features.list.data.repo

import com.masterplus.animals.core.shared_features.database.dao.ListSpeciesDao
import com.masterplus.animals.core.shared_features.database.dao.ListDao
import com.masterplus.animals.core.shared_features.database.entity.ListEntity
import com.masterplus.animals.core.shared_features.list.domain.models.ListModel
import com.masterplus.animals.core.shared_features.list.domain.repo.ListRepo

class ListRepoImpl(
    private val listDao: ListDao,
    private val listSpeciesDao: ListSpeciesDao
): ListRepo {

    override suspend fun insertList(listName: String): Int {
        val pos = getMaxPos()
        val listEntity = ListEntity(
            id = null,
            name = listName,
            isRemovable = true,
            isArchive = false,
            pos = pos,
        )
        return listDao.insertList(listEntity).toInt()
    }

    override suspend fun updateListName(listId: Int, name: String) {
        val listEntity = listDao.getListById(listId) ?: return
        val updatedListEntity = listEntity.copy(name = name)
        listDao.updateList(updatedListEntity)
    }

    override suspend fun archiveList(listId: Int) {
        val listEntity = listDao.getListById(listId) ?: return
        val pos = getMaxPos()
        val updatedListEntity = listEntity.copy(
            isArchive = true,
            pos = pos
        )
        listDao.updateList(updatedListEntity)
    }

    override suspend fun unArchiveList(listId: Int) {
        val listEntity = listDao.getListById(listId) ?: return
        val pos = getMaxPos()
        val updatedListEntity = listEntity.copy(
            isArchive = false,
            pos = pos
        )
        listDao.updateList(updatedListEntity)
    }

    override suspend fun copyList(listModel: ListModel) {
        val newListId = insertList(
            listName = "${listModel.name} Copy"
        )
        val listSpecies = listSpeciesDao.getListSpeciesByListId(listModel.id ?: 0)
        val copiedListSpecies = listSpecies.map {
            it.copy(listId = newListId)
        }
        listSpeciesDao.insertListSpecies(copiedListSpecies)
    }


    override suspend fun deleteListById(listId: Int) {
        listDao.deleteListById(listId)
    }

    override suspend fun isFavoriteList(listId: Int): Boolean {
        return listDao.isFavoriteList(listId)
    }

    private suspend fun getMaxPos(): Int{
        return listDao.getMaxPos() + 1
    }
}