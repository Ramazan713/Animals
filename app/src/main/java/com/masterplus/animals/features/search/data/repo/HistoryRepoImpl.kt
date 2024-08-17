package com.masterplus.animals.features.search.data.repo

import com.masterplus.animals.core.shared_features.database.dao.HistoryDao
import com.masterplus.animals.core.shared_features.database.entity.HistoryEntity
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import com.masterplus.animals.features.search.data.mapper.toHistory
import com.masterplus.animals.features.search.domain.enums.HistoryType
import com.masterplus.animals.features.search.domain.models.History
import com.masterplus.animals.features.search.domain.repo.HistoryRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class HistoryRepoImpl(
    private val historyDao: HistoryDao
): HistoryRepo {

    override fun getHistoryFlow(type: HistoryType, language: LanguageEnum): Flow<List<History>> {
        return historyDao.getFlowHistories(typeId = type.typeId, langCode = language.code)
            .map { items -> items.map { it.toHistory() } }
    }

    override suspend fun insertHistory(content: String, type: HistoryType, language: LanguageEnum) {
        val history = HistoryEntity(
            id = null,
            content = content,
            lang_code = language.code,
            history_type_id = type.typeId,
            modified_date_time = getCurrentIsoDateTime()
        )
        historyDao.insertHistory(history)
    }

    override suspend fun upsertHistory(content: String, type: HistoryType, language: LanguageEnum) {
        val history = historyDao.getHistory(
            typeId = type.typeId,
            langCode = language.code,
            content = content
        )
        if(history != null){
            val updatedHistory = history.copy(
                modified_date_time = getCurrentIsoDateTime()
            )
            historyDao.updateHistory(updatedHistory)
            return
        }
        insertHistory(content, type, language)
    }

    override suspend fun deleteHistoryById(id: Int) {
        historyDao.deleteHistoryById(id)
    }

    private fun getCurrentIsoDateTime(): String{
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toString()
    }
}