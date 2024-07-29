package com.masterplus.animals.core.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.masterplus.animals.core.data.mapper.toClass
import com.masterplus.animals.core.data.mapper.toFamily
import com.masterplus.animals.core.data.mapper.toHabitatCategory
import com.masterplus.animals.core.data.mapper.toOrder
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.models.ClassModel
import com.masterplus.animals.core.domain.models.FamilyModel
import com.masterplus.animals.core.domain.models.HabitatCategoryModel
import com.masterplus.animals.core.domain.models.OrderModel
import com.masterplus.animals.core.domain.repo.CategoryRepo
import com.masterplus.animals.core.shared_features.database.dao.CategoryDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryRepoImpl constructor(
    private val categoryDao: CategoryDao
): CategoryRepo {
    override suspend fun getCategoryName(categoryType: CategoryType, itemId: Int): String? {
        return when(categoryType){
            CategoryType.Habitat -> categoryDao.getHabitatCategoryWithId(itemId)?.habitat_category_tr
            CategoryType.Class -> categoryDao.getClassWithId(itemId)?.scientific_name
            CategoryType.Order -> categoryDao.getOrderWithId(itemId)?.scientific_name
            CategoryType.Family -> categoryDao.getFamilyWithId(itemId)?.scientific_name
        }
    }

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


    override suspend fun getClassWithId(classId: Int): ClassModel? {
        return categoryDao.getClassWithId(classId)?.toClass()
    }


    override suspend fun getOrderWithId(orderId: Int): OrderModel? {
        return categoryDao.getOrderWithId(orderId)?.toOrder()
    }


    override fun getPagingOrdersWithClassId(
        classId: Int,
        pageSize: Int
    ): Flow<PagingData<OrderModel>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = { categoryDao.getPagingOrdersWithClassId(classId) }
        ).flow.map { items -> items.map { it.toOrder() } }
    }

    override fun getPagingFamiliesWithOrderId(
        orderId: Int,
        pageSize: Int
    ): Flow<PagingData<FamilyModel>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = { categoryDao.getPagingFamiliesWithOrderId(orderId) }
        ).flow.map { items -> items.map { it.toFamily() } }
    }

    override fun getPagingClasses(pageSize: Int): Flow<PagingData<ClassModel>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = { categoryDao.getPagingClasses() }
        ).flow.map { items -> items.map { it.toClass() } }
    }

    override fun getPagingOrders(pageSize: Int): Flow<PagingData<OrderModel>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = { categoryDao.getPagingOrders() }
        ).flow.map { items -> items.map { it.toOrder() } }
    }

    override fun getPagingFamilies(pageSize: Int): Flow<PagingData<FamilyModel>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = { categoryDao.getPagingFamilies() }
        ).flow.map { items -> items.map { it.toFamily() } }
    }
}