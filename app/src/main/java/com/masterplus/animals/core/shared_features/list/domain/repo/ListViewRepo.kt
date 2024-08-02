package com.masterplus.animals.core.shared_features.list.domain.repo

import com.masterplus.animals.core.shared_features.list.domain.models.ListView
import com.masterplus.animals.core.shared_features.list.domain.models.SelectableListView
import kotlinx.coroutines.flow.Flow

interface ListViewRepo {

    fun getListViews(isArchive: Boolean): Flow<List<ListView>>

    fun getSelectableListViews(animalId: Int, isArchive: Boolean? = null): Flow<List<SelectableListView>>
}