package com.masterplus.animals.core.shared_features.list.data.repo

import com.masterplus.animals.core.shared_features.list.data.mapper.toListView
import com.masterplus.animals.core.shared_features.list.domain.models.ListView
import com.masterplus.animals.core.shared_features.list.domain.models.SelectableListView
import com.masterplus.animals.core.shared_features.list.domain.repo.ListViewRepo
import com.masterplus.trdictionary.core.data.local.services.ListViewDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class ListViewRepoImpl(
    private val listViewDao: ListViewDao
): ListViewRepo {

    override fun getListViews(isArchive: Boolean): Flow<List<ListView>> {
        return listViewDao.getListViews(isArchive)
            .map { items ->
                items.map { it.toListView() }
            }
    }

    override fun getSelectableListViews(
        animalId: Int,
        isArchive: Boolean?,
    ): Flow<List<SelectableListView>> {
        val removableListViews = if(isArchive != null) listViewDao.getRemovableListViews(isArchive)
            else listViewDao.getRemovableAllListViews()
        val selectedLists = listViewDao.getListViewsByAnimalId(animalId)

        return combine(removableListViews, selectedLists){lists, selecteds ->
            lists.map { listEntity ->
                val isSelected = selecteds.contains(listEntity)
                SelectableListView(
                    listView = listEntity.toListView(),
                    isSelected = isSelected
                )
            }
        }
    }
}