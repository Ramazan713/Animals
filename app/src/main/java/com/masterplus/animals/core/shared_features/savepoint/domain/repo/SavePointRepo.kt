package com.masterplus.animals.core.shared_features.savepoint.domain.repo

import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import com.masterplus.animals.core.shared_features.savepoint.domain.models.SavePoint
import kotlinx.coroutines.flow.Flow

interface SavePointRepo {

    fun getAllSavePointsByContentType(
        contentType: SavePointContentType,
        filteredDestinationTypeIds: List<Int>? = null
    ): Flow<List<SavePoint>>

    fun getContentSavePointsByDestination(destinationTypeId: Int, destinationId: Int? = null): Flow<List<SavePoint>>

    fun getCategorySavePointsByDestination(destinationTypeId: Int, destinationId: Int? = null): Flow<List<SavePoint>>


    suspend fun insertContentSavePoint(
        title: String,
        destination: SavePointDestination,
        itemPosIndex: Int
    )

    suspend fun insertCategorySavePoint(
        title: String,
        destination: SavePointDestination,
        itemPosIndex: Int,
    )

    suspend fun updateSavePointPos(id: Int, posIndex: Int)

    suspend fun updateSavePointTitle(id: Int, title: String)

    suspend fun deleteSavePoint(id: Int)
}