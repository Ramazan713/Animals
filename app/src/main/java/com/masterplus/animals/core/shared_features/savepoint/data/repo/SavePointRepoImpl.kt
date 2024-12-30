package com.masterplus.animals.core.shared_features.savepoint.data.repo

import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.shared_features.database.dao.SavePointDao
import com.masterplus.animals.core.shared_features.database.entity_helper.SavePointWithImageEmbedded
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

    override suspend fun insertSavePoint(
        title: String,
        destination: SavePointDestination,
        itemId: Int,
        contentType: SavePointContentType,
        saveMode: SavePointSaveMode,
        dateTime: LocalDateTime?
    ){
        val imageInfo = categoryImageInfoUseCase(destination)
        val savePoint = SavePoint(
            title = title,
            contentType = contentType,
            itemId = itemId,
            destination = destination,
            modifiedTime = dateTime ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            image = imageInfo.image,
            kingdomType = destination.kingdomType,
            saveMode = saveMode
        )
        savePointDao.insertSavePoint(savePoint.toSavePointEntity())
    }

    override suspend fun updateSavePointPos(id: Int, itemId: Int) {
        savePointDao.updateSavePointItemId(id, itemId)
    }

    override suspend fun updateSavePointTitle(id: Int, title: String) {
        savePointDao.updateSavePointTitle(id, title)
    }

    override suspend fun deleteSavePoint(id: Int) {
        savePointDao.deleteSavePointById(id)
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


    override fun getAllSavePoints(
        contentType: SavePointContentType,
        kingdomType: KingdomType,
        filteredDestinationTypeIds: List<Int>?,
        filterBySaveMode: SavePointSaveMode?
    ): Flow<List<SavePoint>> {
        val kingdomId = kingdomType.kingdomId
        val saveModeId = filterBySaveMode?.modeId
        val savePointsFlow: Flow<List<SavePointWithImageEmbedded>>

        if(filteredDestinationTypeIds != null){
            savePointsFlow = if(saveModeId != null){
                savePointDao.getAllFlowSavePointsByFilteredDestinations(
                    contentTypeId = contentType.contentTypeId,
                    destinationTypeIds = filteredDestinationTypeIds,
                    kingdomId = kingdomId,
                    saveModeId = saveModeId
                )
            }else{
                savePointDao.getAllFlowSavePointsByFilteredDestinations(
                    contentTypeId = contentType.contentTypeId,
                    destinationTypeIds = filteredDestinationTypeIds,
                    kingdomId = kingdomId,
                )
            }
        }else{
            savePointsFlow = if(saveModeId != null){
                savePointDao.getAllFlowSavePointsByContentType(
                    contentTypeId = contentType.contentTypeId,
                    kingdomId = kingdomId,
                    saveModeId = saveModeId
                )
            }else{
                savePointDao.getAllFlowSavePointsByContentType(
                    contentTypeId = contentType.contentTypeId,
                    kingdomId = kingdomId,
                )
            }
        }
        return savePointsFlow.map { items -> items.mapNotNull { it.toSavePoint() } }
    }

    override fun getSavePointsByDestination(
        destinationTypeId: Int,
        contentType: SavePointContentType,
        kingdomType: KingdomType,
        destinationId: Int?,
        filterBySaveMode: SavePointSaveMode?
    ): Flow<List<SavePoint>> {
        val kingdomId = kingdomType.kingdomId
        val saveModeId = filterBySaveMode?.modeId
        val contentTypeId = contentType.contentTypeId
        val savePointsFlow: Flow<List<SavePointWithImageEmbedded>>

        if(destinationId != null){
            if(saveModeId != null){
                savePointsFlow = savePointDao.getFlowSavePointsDestinationByDestId(
                    destinationTypeId = destinationTypeId,
                    destinationId = destinationId,
                    contentTypeId = contentTypeId,
                    kingdomId = kingdomId,
                    saveModeId = saveModeId
                )
            }else{
                savePointsFlow = savePointDao.getFlowSavePointsDestinationByDestId(
                    destinationTypeId = destinationTypeId,
                    destinationId = destinationId,
                    contentTypeId = contentTypeId,
                    kingdomId = kingdomId
                )
            }
        }else{
            savePointsFlow = if(saveModeId != null){
                savePointDao.getFlowSavePointsDestinations(
                    destinationTypeId = destinationTypeId,
                    contentTypeId = contentTypeId,
                    kingdomId = kingdomId,
                    saveModeId = saveModeId
                )
            }else{
                savePointDao.getFlowSavePointsDestinations(
                    destinationTypeId = destinationTypeId,
                    contentTypeId = contentTypeId,
                    kingdomId = kingdomId,
                )
            }
        }

        return savePointsFlow.map { items -> items.mapNotNull { it.toSavePoint() } }
    }




}