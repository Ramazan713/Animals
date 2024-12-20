package com.masterplus.animals.core.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.domain.models.ClassModel
import com.masterplus.animals.core.domain.models.FamilyModel
import com.masterplus.animals.core.domain.models.HabitatCategoryModel
import com.masterplus.animals.core.domain.models.OrderModel
import com.masterplus.animals.core.domain.repo.CategoryRepo
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import com.masterplus.animals.test_utils.FakePagingSource
import kotlinx.coroutines.flow.Flow

class CategoryRepoFake: CategoryRepo{
    var fakeCategories = mutableListOf<CategoryData>()
    var fakeClasses = mutableListOf<ClassModel>()
    var fakeOrders = mutableListOf<OrderModel>()
    var fakeFamilies = mutableListOf<FamilyModel>()
    var fakeHabitats = mutableListOf<HabitatCategoryModel>()

    override suspend fun getCategoryData(
        categoryType: CategoryType,
        limit: Int,
        language: LanguageEnum,
        kingdomType: KingdomType
    ): List<CategoryData> {
        return fakeCategories.filter { it.categoryType == categoryType }.take(limit)
    }

    override suspend fun getCategoryName(categoryType: CategoryType, itemId: Int, language: LanguageEnum): String? {
        val categoryPair = fakeCategories.firstOrNull { it.categoryType == categoryType && it.id == itemId }
        return categoryPair?.title
    }


    override suspend fun getClassWithId(classId: Int, language: LanguageEnum): ClassModel? {
        return fakeClasses.firstOrNull { it.id == classId }
    }

    override suspend fun getOrderWithId(orderId: Int, language: LanguageEnum): OrderModel? {
        return fakeOrders.firstOrNull { it.id == orderId }
    }

    override suspend fun getFamilyWithId(familyId: Int, language: LanguageEnum): FamilyModel? {
        return fakeFamilies.firstOrNull { it.id == familyId }
    }

    override fun getPagingOrdersWithClassId(
        classId: Int,
        pageSize: Int,
        language: LanguageEnum,
        kingdomType: KingdomType
    ): Flow<PagingData<OrderModel>> {
        val filteredOrders = fakeOrders.filter { it.classId == classId && it.kingdomType == kingdomType }
        val fakePagingSource = FakePagingSource(filteredOrders)
        return Pager(PagingConfig(pageSize = pageSize)) { fakePagingSource }.flow
    }

    override fun getPagingFamiliesWithOrderId(
        orderId: Int,
        pageSize: Int,
        language: LanguageEnum,
        kingdomType: KingdomType
    ): Flow<PagingData<FamilyModel>> {
        val filteredFamilies = fakeFamilies.filter { it.orderId == orderId }
        val fakePagingSource = FakePagingSource(filteredFamilies)
        return Pager(PagingConfig(pageSize = pageSize)) { fakePagingSource }.flow
    }

    override fun getPagingClasses(pageSize: Int, language: LanguageEnum, kingdomType: KingdomType): Flow<PagingData<ClassModel>> {
        val fakePagingSource = FakePagingSource(fakeClasses)
        return Pager(PagingConfig(pageSize = pageSize)) { fakePagingSource }.flow
    }

    override fun getPagingOrders(pageSize: Int, language: LanguageEnum, kingdomType: KingdomType): Flow<PagingData<OrderModel>> {
        val fakePagingSource = FakePagingSource(fakeOrders)
        return Pager(PagingConfig(pageSize = pageSize)) { fakePagingSource }.flow
    }

    override fun getPagingHabitats(
        pageSize: Int,
        language: LanguageEnum,
        kingdomType: KingdomType
    ): Flow<PagingData<HabitatCategoryModel>> {
        val fakePagingSource = FakePagingSource(fakeHabitats)
        return Pager(PagingConfig(pageSize = pageSize)) { fakePagingSource }.flow
    }

    override fun getPagingFamilies(pageSize: Int, language: LanguageEnum, kingdomType: KingdomType): Flow<PagingData<FamilyModel>> {
        val fakePagingSource = FakePagingSource(fakeFamilies)
        return Pager(PagingConfig(pageSize = pageSize)) { fakePagingSource }.flow
    }

}