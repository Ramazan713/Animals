package com.masterplus.animals.core.shared_features.list.domain.repo

import com.masterplus.animals.core.shared_features.list.domain.models.ListModel

interface ListRepo {

    suspend fun insertList(listName: String): Int

    suspend fun updateListName(listId: Int, name: String)

    suspend fun copyList(listModel: ListModel)

    suspend fun archiveList(listId: Int)

    suspend fun unArchiveList(listId: Int)

    suspend fun deleteListById(listId: Int)

    suspend fun isFavoriteList(listId: Int): Boolean
}