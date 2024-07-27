package com.masterplus.animals.features.animal.data.repo

import com.masterplus.animals.core.data.mapper.toClass
import com.masterplus.animals.core.data.mapper.toFamily
import com.masterplus.animals.core.data.mapper.toHabitatCategory
import com.masterplus.animals.core.data.mapper.toOrder
import com.masterplus.animals.core.domain.models.ClassModel
import com.masterplus.animals.core.domain.models.FamilyModel
import com.masterplus.animals.core.domain.models.HabitatCategoryModel
import com.masterplus.animals.core.domain.models.OrderModel
import com.masterplus.animals.core.shared_features.database.dao.CategoryDao
import com.masterplus.animals.features.animal.domain.repo.AnimalCategoryRepo

class AnimalCategoryRepoImpl constructor(
    private val categoryDao: CategoryDao
): AnimalCategoryRepo {

    override suspend fun getClasses(limit: Int): List<ClassModel> {
        return categoryDao.getClasses(limit)
            .map { it.toClass() }
    }

    override suspend fun getFamilies(limit: Int): List<FamilyModel> {
        return categoryDao.getFamilies(limit)
            .map { it.toFamily() }
    }

    override suspend fun getHabitatCategories(limit: Int): List<HabitatCategoryModel> {
        return categoryDao.getHabitatCategories(limit)
            .map { it.toHabitatCategory() }
    }

    override suspend fun getOrders(limit: Int): List<OrderModel> {
        return categoryDao.getOrders(limit)
            .map { it.toOrder() }
    }
}