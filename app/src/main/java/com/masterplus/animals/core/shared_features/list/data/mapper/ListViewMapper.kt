package com.masterplus.animals.core.shared_features.list.data.mapper

import com.masterplus.animals.core.shared_features.database.view.ListViewEntity
import com.masterplus.animals.core.shared_features.list.domain.models.ListModel
import com.masterplus.animals.core.shared_features.list.domain.models.ListView

fun ListViewEntity.toListView(): ListView {
    return ListView(
        id = id,
        name = name,
        isRemovable = isRemovable,
        isArchive = isArchive,
        listPos = listPos,
        contentMaxPos = contentMaxPos,
        itemCounts = itemCounts
    )
}

fun ListView.toListModel(): ListModel {
    return ListModel(
        id = id,
        name = name,
        isRemovable = isRemovable,
        isArchive = isArchive,
        pos = listPos,
    )
}





