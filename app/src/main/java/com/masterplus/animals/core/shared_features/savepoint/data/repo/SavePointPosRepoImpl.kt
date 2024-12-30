package com.masterplus.animals.core.shared_features.savepoint.data.repo

import com.masterplus.animals.core.shared_features.database.dao.CategoryDao
import com.masterplus.animals.core.shared_features.database.dao.SpeciesDao
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import com.masterplus.animals.core.shared_features.savepoint.domain.repo.SavePointPosRepo


class SavePointPosRepoImpl(
    private val speciesDao: SpeciesDao,
    private val categoryDao: CategoryDao
): SavePointPosRepo {

    override suspend fun getItemPos(
        itemId: Int,
        destination: SavePointDestination,
        contentType: SavePointContentType
    ): Int? {
        val label = destination.toLabel(
            contentType = contentType
        )

        if(contentType.isContent){
            return speciesDao.getSpeciesByIdAndLabel(itemId, label)?.let {
                speciesDao.getSpeciesPosByLabel(itemId, label)
            }
        }
        return when(destination){
            is SavePointDestination.All -> null
            is SavePointDestination.ClassType -> {
                categoryDao.getClassWithId2(itemId, label)?.let {
                    categoryDao.getClassPosByLabel(itemId, label)
                }
            }
            is SavePointDestination.Family -> {
                categoryDao.getFamilyWithId2(itemId, label)?.let {
                    categoryDao.getFamilyPosByLabel(itemId, label)
                }
            }
            is SavePointDestination.Habitat -> {
                categoryDao.getHabitatCategoryWithId2(itemId, label)?.let {
                    categoryDao.getHabitatPosByLabel(itemId, label)
                }
            }
            is SavePointDestination.ListType -> {
                categoryDao.getFamilyWithId2(itemId, label)?.let {
                    categoryDao.getFamilyPosByLabel(itemId, label)
                }
            }
            is SavePointDestination.Order -> {
                categoryDao.getOrderWithId2(itemId, label)?.let {
                    categoryDao.getOrderPosByLabel(itemId, label)
                }
            }
        }
    }
}