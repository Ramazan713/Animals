package com.masterplus.animals.features.search.domain.repo

import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import com.masterplus.animals.features.search.domain.enums.HistoryType
import com.masterplus.animals.features.search.domain.models.History
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

interface HistoryRepo {

    fun getHistoryFlow(
        type: HistoryType,
        language: LanguageEnum
    ): Flow<List<History>>

    suspend fun insertHistory(
        content: String,
        type: HistoryType,
        language: LanguageEnum
    )

    suspend fun upsertHistory(
        content: String,
        type: HistoryType,
        language: LanguageEnum
    )

    suspend fun deleteHistoryById(id: Int)
}