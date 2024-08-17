package com.masterplus.animals.core.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.masterplus.animals.core.data.mapper.toCategoryData
import com.masterplus.animals.core.data.mapper.toClass
import com.masterplus.animals.core.data.mapper.toFamily
import com.masterplus.animals.core.data.mapper.toHabitatCategory
import com.masterplus.animals.core.data.mapper.toOrder
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.domain.models.ClassModel
import com.masterplus.animals.core.domain.models.FamilyModel
import com.masterplus.animals.core.domain.models.OrderModel
import com.masterplus.animals.core.domain.repo.CategoryRepo
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.shared_features.database.dao.CategoryDao
import com.masterplus.animals.core.shared_features.database.dao.ListDao
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryRepoImpl constructor(
    private val categoryDao: CategoryDao,
    private val listDao: ListDao
): CategoryRepo {
    override suspend fun getCategoryData(
        categoryType: CategoryType,
        limit: Int,
        language: LanguageEnum
    ): List<CategoryData> {
        return when(categoryType){
            CategoryType.Habitat -> categoryDao.getHabitatCategories(limit).map { it.toHabitatCategory(language).toCategoryData() }
            CategoryType.Class -> categoryDao.getClasses(limit).map { it.toClass(language).toCategoryData() }
            CategoryType.Order -> categoryDao.getOrders(limit).map { it.toOrder(language).toCategoryData() }
            CategoryType.Family -> categoryDao.getFamilies(limit).map { it.toFamily(language).toCategoryData() }
            CategoryType.List -> emptyList()
        }
    }

    override suspend fun getCategoryName(categoryType: CategoryType, itemId: Int, language: LanguageEnum): UiText? {
        val title = when(categoryType){
            CategoryType.Habitat -> categoryDao.getHabitatCategoryWithId(itemId)?.let { if(language.isEn) it.habitat_category_en else it.habitat_category_tr }
            CategoryType.Class -> categoryDao.getClassWithId(itemId)?.scientific_name
            CategoryType.Order -> categoryDao.getOrderWithId(itemId)?.scientific_name
            CategoryType.Family -> categoryDao.getFamilyWithId(itemId)?.scientific_name
            CategoryType.List -> listDao.getListById(itemId)?.name
        }
        return if(title != null) UiText.Text(title) else null
    }


    override suspend fun getClassWithId(classId: Int, language: LanguageEnum): ClassModel? {
        return categoryDao.getClassWithId(classId)?.toClass(language)
    }


    override suspend fun getOrderWithId(orderId: Int, language: LanguageEnum): OrderModel? {
        return categoryDao.getOrderWithId(orderId)?.toOrder(language)
    }


    override fun getPagingOrdersWithClassId(
        classId: Int,
        pageSize: Int,
        language: LanguageEnum
    ): Flow<PagingData<OrderModel>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = { categoryDao.getPagingOrdersWithClassId(classId) }
        ).flow.map { items -> items.map { it.toOrder(language) } }
    }

    override fun getPagingFamiliesWithOrderId(
        orderId: Int,
        pageSize: Int,
        language: LanguageEnum
    ): Flow<PagingData<FamilyModel>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = { categoryDao.getPagingFamiliesWithOrderId(orderId) }
        ).flow.map { items -> items.map { it.toFamily(language) } }
    }

    override fun getPagingClasses(pageSize: Int, language: LanguageEnum): Flow<PagingData<ClassModel>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = { categoryDao.getPagingClasses() }
        ).flow.map { items -> items.map { it.toClass(language) } }
    }

    override fun getPagingOrders(pageSize: Int, language: LanguageEnum): Flow<PagingData<OrderModel>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = { categoryDao.getPagingOrders() }
        ).flow.map { items -> items.map { it.toOrder(language) } }
    }

    override fun getPagingFamilies(pageSize: Int, language: LanguageEnum): Flow<PagingData<FamilyModel>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = { categoryDao.getPagingFamilies() }
        ).flow.map { items -> items.map { it.toFamily(language) } }
    }
}