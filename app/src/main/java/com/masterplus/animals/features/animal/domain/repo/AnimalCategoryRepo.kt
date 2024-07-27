package com.masterplus.animals.features.animal.domain.repo

import com.masterplus.animals.core.domain.models.ClassModel
import com.masterplus.animals.core.domain.models.FamilyModel
import com.masterplus.animals.core.domain.models.HabitatCategoryModel
import com.masterplus.animals.core.domain.models.OrderModel

interface AnimalCategoryRepo {
    suspend fun getClasses(limit: Int): List<ClassModel>

    suspend fun getFamilies(limit: Int): List<FamilyModel>

    suspend fun getHabitatCategories(limit: Int): List<HabitatCategoryModel>

    suspend fun getOrders(limit: Int): List<OrderModel>
}