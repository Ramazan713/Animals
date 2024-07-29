package com.masterplus.animals.core.domain.repo

import androidx.paging.PagingData
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.models.ClassModel
import com.masterplus.animals.core.domain.models.FamilyModel
import com.masterplus.animals.core.domain.models.HabitatCategoryModel
import com.masterplus.animals.core.domain.models.OrderModel
import kotlinx.coroutines.flow.Flow

interface CategoryRepo {

    suspend fun getCategoryName(categoryType: CategoryType, itemId: Int): String?

    suspend fun getClasses(limit: Int): List<ClassModel>

    suspend fun getFamilies(limit: Int): List<FamilyModel>

    suspend fun getHabitatCategories(limit: Int): List<HabitatCategoryModel>

    suspend fun getOrders(limit: Int): List<OrderModel>


    suspend fun getClassWithId(classId: Int): ClassModel?

    suspend fun getOrderWithId(orderId: Int): OrderModel?

    fun getPagingOrdersWithClassId(classId: Int, pageSize: Int): Flow<PagingData<OrderModel>>

    fun getPagingFamiliesWithOrderId(orderId: Int, pageSize: Int): Flow<PagingData<FamilyModel>>



    fun getPagingClasses(pageSize: Int): Flow<PagingData<ClassModel>>

    fun getPagingFamilies(pageSize: Int): Flow<PagingData<FamilyModel>>

    fun getPagingOrders(pageSize: Int): Flow<PagingData<OrderModel>>

}