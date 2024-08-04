package com.masterplus.animals.core.shared_features.savepoint.data.repo

import com.masterplus.animals.core.shared_features.database.dao.SavePointDao
import com.masterplus.animals.core.shared_features.savepoint.data.mapper.toSavePoint
import com.masterplus.animals.core.shared_features.savepoint.data.mapper.toSavePointEntity
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import com.masterplus.animals.core.shared_features.savepoint.domain.models.SavePoint
import com.masterplus.animals.core.shared_features.savepoint.domain.repo.SavePointRepo
import com.masterplus.animals.core.shared_features.savepoint.domain.use_cases.SavePointCategoryImageInfoUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class SavePointRepoImpl(
    private val savePointDao: SavePointDao,
    private val categoryImageInfoUseCase: SavePointCategoryImageInfoUseCase
): SavePointRepo {

    override fun getAllSavePointsByContentType(
        contentType: SavePointContentType,
        filteredDestinationTypeIds: List<Int>?
    ): Flow<List<SavePoint>> {
        val savePointsFlow = if(filteredDestinationTypeIds != null)
            savePointDao.getAllFlowSavePointsByFilteredDestinations(contentType.contentTypeId, filteredDestinationTypeIds) else
                savePointDao.getAllFlowSavePointsByContentType(contentType.contentTypeId)
        return savePointsFlow.map { items -> items.mapNotNull { it.toSavePoint() } }
    }

    override fun getContentSavePointsByDestination(
        destinationTypeId: Int,
        destinationId: Int?
    ): Flow<List<SavePoint>> {
        return getSavePointsByDestination(
            contentType = SavePointContentType.Content,
            destinationTypeId = destinationTypeId,
            destinationId = destinationId
        )
    }

    override fun getCategorySavePointsByDestination(
        destinationTypeId: Int,
        destinationId: Int?
    ): Flow<List<SavePoint>> {
        return getSavePointsByDestination(
            contentType = SavePointContentType.Category,
            destinationTypeId = destinationTypeId,
            destinationId = destinationId
        )
    }

    override suspend fun insertContentSavePoint(
        title: String,
        destination: SavePointDestination,
        itemPosIndex: Int
    ) {
        insertSavePoint(
            title = title,
            destination = destination,
            itemPosIndex = itemPosIndex,
            contentType = SavePointContentType.Content,
        )
    }

    override suspend fun insertCategorySavePoint(
        title: String,
        destination: SavePointDestination,
        itemPosIndex: Int
    ) {
        insertSavePoint(
            title = title,
            destination = destination,
            itemPosIndex = itemPosIndex,
            contentType = SavePointContentType.Category,
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
        destinationId: Int?
    ): Flow<List<SavePoint>> {
        return if(destinationId != null){
            savePointDao
                .getFlowSavePointsDestinationByDestId(destinationTypeId, destinationId, contentType.contentTypeId)
        }else{
            savePointDao
                .getFlowSavePointsDestinations(destinationTypeId, contentType.contentTypeId)
        }.map { items -> items.mapNotNull { it.toSavePoint() } }
    }

    private suspend fun insertSavePoint(
        title: String,
        destination: SavePointDestination,
        itemPosIndex: Int,
        contentType: SavePointContentType
    ){
        val imageInfo = categoryImageInfoUseCase(destination)
        val savePoint = SavePoint(
            title = title,
            contentType = contentType,
            itemPosIndex = itemPosIndex,
            destination = destination,
            modifiedTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
            imagePath = imageInfo.imagePath,
            imageUrl = imageInfo.imageUrl,
        )
        savePointDao.insertSavePoint(savePoint.toSavePointEntity())
    }

}