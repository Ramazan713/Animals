package com.masterplus.animals.core.shared_features.list.data.mapper

import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.domain.models.CategoryDataType
import com.masterplus.animals.core.shared_features.database.entity.ListEntity
import com.masterplus.animals.core.shared_features.list.domain.models.ListModel


fun ListEntity.toListModel(): ListModel {
    return ListModel(
        id = id,
        name = name,
        isArchive = isArchive,
        isRemovable = isRemovable,
        pos = pos,
    )
}


fun ListModel.toListEntity(): ListEntity {
    return ListEntity(
        id = id,
        name = name,
        isArchive = isArchive,
        isRemovable = isRemovable,
        pos = pos
    )
}

fun ListModel.toCategoryData(): CategoryData {
    return CategoryData(
        id = id ?: 0,
        image = null,
        title = name,
        categoryDataType = CategoryDataType.List,
        kingdomType = KingdomType.DEFAULT,
        orderKey = pos
    )
}