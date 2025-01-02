package com.masterplus.animals.core.shared_features.savepoint.data.repo

import com.masterplus.animals.core.shared_features.database.dao.CategoryDao
import com.masterplus.animals.core.shared_features.database.dao.SavePointPosDao
import com.masterplus.animals.core.shared_features.database.dao.SpeciesDao
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import com.masterplus.animals.core.shared_features.savepoint.domain.repo.SavePointPosRepo


class SavePointPosRepoImpl(
    private val speciesDao: SpeciesDao,
    private val categoryDao: CategoryDao,
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
            return speciesDao.getSpeciesByIdAndLabel(orderKey, label)?.let {
                savePointPosDao.getSpeciesPosByLabel(orderKey, label)
            }
        }
        return when(destination){
            is SavePointDestination.All -> null
            is SavePointDestination.ClassType -> {
                categoryDao.getClassWithId2(orderKey, label)?.let {
                    savePointPosDao.getClassPosByLabel(orderKey, label)
                }
            }
            is SavePointDestination.Family -> {
                categoryDao.getFamilyWithId2(orderKey, label)?.let {
                    savePointPosDao.getFamilyPosByLabel(orderKey, label)
                }
            }
            is SavePointDestination.Habitat -> {
                categoryDao.getHabitatCategoryWithId2(orderKey, label)?.let {
                    savePointPosDao.getHabitatPosByLabel(orderKey, label)
                }
            }
            is SavePointDestination.ListType -> {
                categoryDao.getFamilyWithId2(orderKey, label)?.let {
                    savePointPosDao.getFamilyPosByLabel(orderKey, label)
                }
            }
            is SavePointDestination.Order -> {
                categoryDao.getOrderWithId2(orderKey, label)?.let {
                    savePointPosDao.getOrderPosByLabel(orderKey, label)
                }
            }
        }
    }
}