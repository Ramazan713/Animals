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

    override suspend fun insertSavePoint(
        title: String,
        destination: SavePointDestination,
        orderKey: Int,
        contentType: SavePointContentType,
        saveMode: SavePointSaveMode,
        dateTime: LocalDateTime?
    ){
        val imageInfo = categoryImageInfoUseCase(destination)
        val savePoint = SavePoint(
            title = title,
            contentType = contentType,
            orderKey = orderKey,
            destination = destination,
            modifiedTime = dateTime ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            image = imageInfo.image,
            kingdomType = destination.kingdomType,
            saveMode = saveMode
        )
        savePointDao.insertSavePoint(savePoint.toSavePointEntity())
    }

    override suspend fun updateSavePointPos(id: Int, orderKey: Int) {
        savePointDao.updateSavePointItemId(id, orderKey)
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

    override fun getSavePoints(
        savePointDestination: SavePointDestination,
        contentType: SavePointContentType,
    ): Flow<List<SavePoint>> {
        val savePointsFlow = with(savePointDestination){
            if(destinationId != null){
                savePointDao
                    .getSavePoints(
                        contentTypeId = contentType.contentTypeId,
                        kingdomId = kingdomType.kingdomId,
                        destinationTypeId = destinationTypeId,
                        destinationId = destinationId
                    )
            }else{
                savePointDao
                    .getSavePoints(
                        contentTypeId = contentType.contentTypeId,
                        kingdomId = kingdomType.kingdomId,
                        destinationTypeId = destinationTypeId,
                    )
            }
        }
        return savePointsFlow.map { items -> items.mapNotNull { it.toSavePoint() } }
    }

    override fun getSavePointsByKingdom(
        contentType: SavePointContentType,
        kingdomType: KingdomType,
        filterBySaveMode: SavePointSaveMode?
    ): Flow<List<SavePoint>> {
        val contentTypeId = contentType.contentTypeId
        val kingdomId = kingdomType.kingdomId
        val saveModeId = filterBySaveMode?.modeId
        return if (saveModeId != null){
            savePointDao.getSavePointsByKingdom(
                contentTypeId = contentTypeId,
                kingdomId = kingdomId,
                saveModeId = saveModeId
            )
        }else{
            savePointDao.getSavePointsByKingdom(
                contentTypeId = contentTypeId,
                kingdomId = kingdomId,
            )
        }.map { items -> items.mapNotNull { it.toSavePoint() } }
    }
}