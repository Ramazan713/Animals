package com.masterplus.animals.core.shared_features.savepoint.data.repo

import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointSaveMode
import com.masterplus.animals.core.shared_features.savepoint.domain.models.SavePoint
import com.masterplus.animals.core.shared_features.savepoint.domain.repo.SavePointRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class FakeSavePointRepo : SavePointRepo {

    private var savePointsMutable = mutableListOf<SavePoint>()
    val savePoints get() = savePointsMutable.toMutableList()

    override suspend fun insertSavePoint(
        title: String,
        destination: SavePointDestination,
        orderKey: Int,
        contentType: SavePointContentType,
        saveMode: SavePointSaveMode,
        dateTime: LocalDateTime?
    ) {
        val savePoint = SavePoint(
            id = savePointsMutable.size + 1,
            title = title,
            contentType = contentType,
            orderKey = orderKey,
            destination = destination,
            modifiedTime = dateTime ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            kingdomType = destination.kingdomType,
            saveMode = saveMode,
            image = null
        )
        savePointsMutable.add(savePoint)
    }

    override suspend fun updateSavePointPos(id: Int, orderKey: Int) {
        val savePoint = savePointsMutable.find { it.id == id }
        savePoint?.orderKey = orderKey
    }

    override suspend fun updateSavePointTitle(id: Int, title: String) {
        val savePoint = savePointsMutable.find { it.id == id }
        savePoint?.title = title
    }

    override suspend fun deleteSavePoint(id: Int) {
        savePointsMutable.removeIf { it.id == id }
    }

    override suspend fun getSavePointByQuery(
        destination: SavePointDestination,
        saveMode: SavePointSaveMode,
        contentType: SavePointContentType
    ): SavePoint? {
        return savePointsMutable.find {
            it.destination == destination &&
                    it.saveMode == saveMode &&
                    it.contentType == contentType
        }
    }

    override fun getSavePoints(
        savePointDestination: SavePointDestination,
        contentType: SavePointContentType
    ): Flow<List<SavePoint>> {
        return flow {
            emit(savePointsMutable.filter { it.contentType == contentType && it.destination == savePointDestination })
        }
    }

    override fun getSavePointsByKingdom(
        contentType: SavePointContentType,
        kingdomType: KingdomType,
        filterBySaveMode: SavePointSaveMode?
    ): Flow<List<SavePoint>> {
        return flow {
            emit(
                savePointsMutable.filter {
                    it.contentType == contentType &&
                            it.kingdomType == kingdomType &&
                            (filterBySaveMode == null || it.saveMode == filterBySaveMode)
                }
            )
        }
    }
}