package com.masterplus.animals.core.shared_features.savepoint.data.repo

import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.shared_features.database.dao.SavePointDao
import com.masterplus.animals.core.shared_features.savepoint.data.mapper.toSavePoint
import com.masterplus.animals.core.shared_features.savepoint.data.mapper.toSavePointEntity
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointSaveMode
import com.masterplus.animals.core.shared_features.savepoint.domain.models.SavePoint
import com.masterplus.animals.core.shared_features.savepoint.domain.repo.SavePointRepo
import com.masterplus.animals.core.shared_features.savepoint.domain.use_cases.SavePointCategoryImageInfoUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class SavePointRepoImpl(
    private val savePointDao: SavePointDao,
    private val categoryImageInfoUseCase: SavePointCategoryImageInfoUseCase
): SavePointRepo {

    override fun getAllSavePointsByContentType(
        contentType: SavePointContentType,
        filteredDestinationTypeIds: List<Int>?,
        kingdomType: KingdomType
    ): Flow<List<SavePoint>> {
        val savePointsFlow = if(filteredDestinationTypeIds != null)
            savePointDao.getAllFlowSavePointsByFilteredDestinations(contentType.contentTypeId, filteredDestinationTypeIds, kingdomType.kingdomId) else
                savePointDao.getAllFlowSavePointsByContentType(contentType.contentTypeId, kingdomType.kingdomId)
        return savePointsFlow.map { items -> items.mapNotNull { it.toSavePoint() } }
    }

    override fun getContentSavePointsByDestination(
        destinationTypeId: Int,
        kingdomType: KingdomType,
        destinationId: Int?
    ): Flow<List<SavePoint>> {
        return getSavePointsByDestination(
            contentType = SavePointContentType.Content,
            destinationTypeId = destinationTypeId,
            destinationId = destinationId,
            kingdomType = kingdomType
        )
    }

    override fun getCategorySavePointsByDestination(
        destinationTypeId: Int,
        kingdomType: KingdomType,
        destinationId: Int?,
    ): Flow<List<SavePoint>> {
        return getSavePointsByDestination(
            contentType = SavePointContentType.Category,
            destinationTypeId = destinationTypeId,
            destinationId = destinationId,
            kingdomType = kingdomType
        )
    }

    override suspend fun getSavePointByQuery(
        destination: SavePointDestination,
        saveMode: SavePointSaveMode,
        contentType: SavePointContentType
    ): SavePoint? {
        val savePointEntity = with(destination){
            if(destinationId != null) savePointDao.getSavePointByQuery(
                destinationTypeId = destinationTypeId,
                destinationId = destinationId,
                contentTypeId = contentType.contentTypeId,
                kingdomId = kingdomType.kingdomId,
                saveModeId = saveMode.modeId
            )
            else savePointDao.getSavePointByQuery(
                destinationTypeId = destinationTypeId,
                contentTypeId = contentType.contentTypeId,
                kingdomId = kingdomType.kingdomId,
                saveModeId = saveMode.modeId
            )
        }
        return savePointEntity?.toSavePoint()
    }

    override suspend fun insertContentSavePoint(
        title: String,
        destination: SavePointDestination,
        itemPosIndex: Int,
        saveMode: SavePointSaveMode,
        dateTime: LocalDateTime?
    ) {
        insertSavePoint(
            title = title,
            destination = destination,
            itemPosIndex = itemPosIndex,
            contentType = SavePointContentType.Content,
            dateTime = dateTime,
            saveMode = saveMode
        )
    }

    override suspend fun insertCategorySavePoint(
        title: String,
        destination: SavePointDestination,
        itemPosIndex: Int,
        saveMode: SavePointSaveMode,
        dateTime: LocalDateTime?
    ) {
        insertSavePoint(
            title = title,
            destination = destination,
            itemPosIndex = itemPosIndex,
            contentType = SavePointContentType.Category,
            dateTime = dateTime,
            saveMode = saveMode
        )
    }

    override suspend fun updateSavePointPos(id: Int, posIndex: Int) {
        savePointDao.updateSavePointPos(id, posIndex)
    }

    override suspend fun updateSavePointTitle(id: Int, title: String) {
        savePointDao.updateSavePointTitle(id, title)
    }

    override suspend fun deleteSavePoint(id: Int) {
        savePointDao.deleteSavePointById(id)
    }


    private fun getSavePointsByDestination(
        contentType: SavePointContentType,
        destinationTypeId: Int,
        destinationId: Int?,
        kingdomType: KingdomType
    ): Flow<List<SavePoint>> {
        return if(destinationId != null){
            savePointDao
                .getFlowSavePointsDestinationByDestId(destinationTypeId, destinationId, contentType.contentTypeId, kingdomType.kingdomId)
        }else{
            savePointDao
                .getFlowSavePointsDestinations(destinationTypeId, contentType.contentTypeId, kingdomType.kingdomId)
        }.map { items -> items.mapNotNull { it.toSavePoint() } }
    }

    private suspend fun insertSavePoint(
        title: String,
        destination: SavePointDestination,
        itemPosIndex: Int,
        contentType: SavePointContentType,
        saveMode: SavePointSaveMode,
        dateTime: LocalDateTime?
    ){
        val imageInfo = categoryImageInfoUseCase(destination)
        val savePoint = SavePoint(
            title = title,
            contentType = contentType,
            itemPosIndex = itemPosIndex,
            destination = destination,
            modifiedTime = dateTime ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            imagePath = imageInfo.imagePath,
            imageData = imageInfo.imageUrl,
            kingdomType = destination.kingdomType,
            saveMode = saveMode
        )
        savePointDao.insertSavePoint(savePoint.toSavePointEntity())
    }

}