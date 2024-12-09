package com.masterplus.animals.core.shared_features.savepoint.domain.use_cases

import com.masterplus.animals.core.shared_features.database.dao.CategoryDao
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination

class SavePointCategoryImageInfoUseCase(
    private val categoryDao: CategoryDao
) {

    suspend operator fun invoke(destination: SavePointDestination): InfoResult{
        val nullableResult = InfoResult(null,null)
        val destinationId = destination.destinationId ?: return nullableResult

        when(destination){
            is SavePointDestination.All -> return nullableResult
            is SavePointDestination.ListType -> return nullableResult
            is SavePointDestination.Habitat -> return nullableResult
            is SavePointDestination.ClassType -> {
                val classEntity = categoryDao.getClassWithId(destinationId) ?: return nullableResult
                return InfoResult(classEntity.image_path, classEntity.image_url)
            }
            is SavePointDestination.Family -> {
                val family = categoryDao.getFamilyWithId(destinationId) ?: return nullableResult
                return InfoResult(family.image_path, family.image_url)
            }
            is SavePointDestination.Order -> {
                val order = categoryDao.getOrderWithId(destinationId) ?: return nullableResult
                return InfoResult(order.image_path, order.image_url)
            }
        }
    }

    companion object {
        data class InfoResult(
            val imagePath: String?,
            val imageUrl: String?
        )
    }
}