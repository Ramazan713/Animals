package com.masterplus.animals.core.shared_features.savepoint.data.repo

import com.masterplus.animals.core.shared_features.database.dao.SavePointPosDao
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import com.masterplus.animals.core.shared_features.savepoint.domain.repo.SavePointPosRepo


class SavePointPosRepoImpl(
    private val savePointPosDao: SavePointPosDao
): SavePointPosRepo {

    override suspend fun getItemPos(
        orderKey: Int,
        destination: SavePointDestination,
        contentType: SavePointContentType
    ): Int? {
        val label = destination.toLabel(
            contentType = contentType
        )

        if(contentType.isContent){
            return savePointPosDao.getSpeciesByOrderKeyAndLabel(orderKey, label)?.let {
                savePointPosDao.getSpeciesPosByLabel(orderKey, label)
            }
        }
        return when(destination){
            is SavePointDestination.All -> null
            is SavePointDestination.ClassType -> {
                savePointPosDao.getClassWithOrderKey(orderKey, label)?.let {
                    savePointPosDao.getClassPosByLabel(orderKey, label)
                }
            }
            is SavePointDestination.Family -> {
                savePointPosDao.getFamilyWithOrderKey(orderKey, label)?.let {
                    savePointPosDao.getFamilyPosByLabel(orderKey, label)
                }
            }
            is SavePointDestination.Habitat -> {
                savePointPosDao.getHabitatCategoryWithOrderKey(orderKey, label)?.let {
                    savePointPosDao.getHabitatPosByLabel(orderKey, label)
                }
            }
            is SavePointDestination.ListType -> {
                savePointPosDao.getFamilyWithOrderKey(orderKey, label)?.let {
                    savePointPosDao.getFamilyPosByLabel(orderKey, label)
                }
            }
            is SavePointDestination.Order -> {
                savePointPosDao.getOrderWithOrderKey(orderKey, label)?.let {
                    savePointPosDao.getOrderPosByLabel(orderKey, label)
                }
            }
        }
    }
}