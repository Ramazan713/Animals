package com.masterplus.animals.core.shared_features.savepoint.domain.repo

import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointSaveMode
import com.masterplus.animals.core.shared_features.savepoint.domain.models.SavePoint
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

interface SavePointRepo {

    suspend fun insertSavePoint(
        title: String,
        destination: SavePointDestination,
        orderKey: Int,
        contentType: SavePointContentType,
        saveMode: SavePointSaveMode,
        dateTime: LocalDateTime? = null
    )

    suspend fun updateSavePointPos(id: Int, orderKey: Int)

    suspend fun updateSavePointTitle(id: Int, title: String)

    suspend fun deleteSavePoint(id: Int)

    suspend fun getSavePointByQuery(
        destination: SavePointDestination,
        saveMode: SavePointSaveMode,
        contentType: SavePointContentType
    ): SavePoint?

    fun getSavePoints(
        savePointDestination: SavePointDestination,
        contentType: SavePointContentType,
    ): Flow<List<SavePoint>>

    fun getSavePointsByKingdom(
        contentType: SavePointContentType,
        kingdomType: KingdomType,
        filterBySaveMode: SavePointSaveMode? = null
    ): Flow<List<SavePoint>>
}