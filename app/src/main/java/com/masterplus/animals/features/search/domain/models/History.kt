package com.masterplus.animals.features.search.domain.models

import com.masterplus.animals.features.search.domain.enums.HistoryType
import kotlinx.datetime.LocalDateTime

data class History(
    val id: Int?,
    val content: String,
    val modifiedDateTime: LocalDateTime,
    val historyType: HistoryType
)
