package com.masterplus.animals.features.search.data.mapper

import com.masterplus.animals.core.shared_features.database.entity.HistoryEntity
import com.masterplus.animals.features.search.domain.enums.HistoryType
import com.masterplus.animals.features.search.domain.models.History
import kotlinx.datetime.LocalDateTime

fun HistoryEntity.toHistory(): History{
    return History(
        id = id,
        content = content,
        modifiedDateTime = LocalDateTime.parse(modified_date_time),
        historyType = HistoryType.fromTypeId(history_type_id)
    )
}