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

    override suspend fun insertContentSavePoint(
        title: String,
        destination: SavePointDestination,
        itemPosIndex: Int,
        saveMode: SavePointSaveMode,
        dateTime: LocalDateTime?
    ) {
        val savePoint = SavePoint(
            id = savePointsMutable.size + 1,
            title = title,
            contentType = SavePointContentType.Content,
            itemPosIndex = itemPosIndex,
            destination = destination,
            modifiedTime = dateTime ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            imagePath = null,
            imageData = null,
            kingdomType = destination.kingdomType,
            saveMode = saveMode
        )
        savePointsMutable.add(savePoint)
    }

    override suspend fun insertCategorySavePoint(
        title: String,
        destination: SavePointDestination,
        itemPosIndex: Int,
        saveMode: SavePointSaveMode,
        dateTime: LocalDateTime?
    ) {
        val savePoint = SavePoint(
            id = savePointsMutable.size + 1,
            title = title,
            contentType = SavePointContentType.Category,
            itemPosIndex = itemPosIndex,
            destination = destination,
            modifiedTime = dateTime ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            imagePath = null,
            imageData = null,
            kingdomType = destination.kingdomType,
            saveMode = saveMode
        )
        savePointsMutable.add(savePoint)
    }

    override suspend fun updateSavePointPos(id: Int, posIndex: Int) {
        val savePoint = savePointsMutable.find { it.id == id }
        savePoint?.itemPosIndex = posIndex
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

    override fun getAllSavePoints(
        contentType: SavePointContentType,
        kingdomType: KingdomType,
        filteredDestinationTypeIds: List<Int>?,
        filterBySaveMode: SavePointSaveMode?
    ): Flow<List<SavePoint>> {
        return flow {
            emit(
                savePointsMutable.filter {
                    it.contentType == contentType &&
                            it.kingdomType == kingdomType &&
                            (filteredDestinationTypeIds == null || filteredDestinationTypeIds.contains(it.destination.destinationTypeId)) &&
                            (filterBySaveMode == null || it.saveMode == filterBySaveMode)
                }
            )
        }
    }

    override fun getSavePointsByDestination(
        destinationTypeId: Int,
        contentType: SavePointContentType,
        kingdomType: KingdomType,
        destinationId: Int?,
        filterBySaveMode: SavePointSaveMode?
    ): Flow<List<SavePoint>> {
        return flow {
            emit(
                savePointsMutable.filter {
                    it.destination.destinationTypeId == destinationTypeId &&
                            it.contentType == contentType &&
                            it.kingdomType == kingdomType &&
                            (destinationId == null || it.destination.destinationId == destinationId) &&
                            (filterBySaveMode == null || it.saveMode == filterBySaveMode)
                }
            )
        }
    }
}