package com.masterplus.animals.core.shared_features.savepoint.domain.repo

import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import com.masterplus.animals.core.shared_features.savepoint.domain.models.SavePoint
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

interface SavePointRepo {

    fun getAllSavePointsByContentType(
        contentType: SavePointContentType,
        filteredDestinationTypeIds: List<Int>? = null,
        kingdomType: KingdomType
    ): Flow<List<SavePoint>>

    fun getContentSavePointsByDestination(
        destinationTypeId: Int,
        kingdomType: KingdomType,
        destinationId: Int? = null
    ): Flow<List<SavePoint>>

    fun getCategorySavePointsByDestination(
        destinationTypeId: Int,
        kingdomType: KingdomType,
        destinationId: Int? = null
    ): Flow<List<SavePoint>>


    suspend fun insertContentSavePoint(
        title: String,
        destination: SavePointDestination,
        itemPosIndex: Int,
        dateTime: LocalDateTime? = null
    )

    suspend fun insertCategorySavePoint(
        title: String,
        destination: SavePointDestination,
        itemPosIndex: Int,
        dateTime: LocalDateTime? = null
    )

    suspend fun updateSavePointPos(id: Int, posIndex: Int)

    suspend fun updateSavePointTitle(id: Int, title: String)

    suspend fun deleteSavePoint(id: Int)
}