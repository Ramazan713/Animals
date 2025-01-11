package com.masterplus.animals.core.domain.repo

import androidx.paging.PagingData
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.domain.models.ClassModel
import com.masterplus.animals.core.domain.models.FamilyModel
import com.masterplus.animals.core.domain.models.HabitatCategoryModel
import com.masterplus.animals.core.domain.models.OrderModel
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import kotlinx.coroutines.flow.Flow

interface CategoryRepo {

    suspend fun getCategoryData(
        categoryType: CategoryType,
        limit: Int,
        language: LanguageEnum,
        kingdomType: KingdomType
    ): DefaultResult<List<CategoryData>>

    suspend fun getCategoryName(categoryType: CategoryType, itemId: Int, language: LanguageEnum): String?


    suspend fun getClassWithId(classId: Int, language: LanguageEnum): ClassModel?

    suspend fun getOrderWithId(orderId: Int, language: LanguageEnum): OrderModel?

    suspend fun getFamilyWithId(familyId: Int, language: LanguageEnum): FamilyModel?

    suspend fun getHabitatWithId(habitatId: Int, language: LanguageEnum): HabitatCategoryModel?



    fun getPagingOrdersWithClassId(
        classId: Int,
        pageSize: Int,
        language: LanguageEnum,
        kingdomType: KingdomType,
        targetItemId: Int? = null
    ): Flow<PagingData<OrderModel>>

    fun getPagingFamiliesWithOrderId(
        orderId: Int,
        pageSize: Int,
        language: LanguageEnum,
        kingdomType: KingdomType,
        targetItemId: Int? = null
    ): Flow<PagingData<FamilyModel>>


    fun getPagingClasses(
        pageSize: Int,
        language: LanguageEnum,
        kingdomType: KingdomType,
        targetItemId: Int? = null
    ): Flow<PagingData<ClassModel>>

    fun getPagingFamilies(
        pageSize: Int,
        language: LanguageEnum,
        kingdomType: KingdomType,
        targetItemId: Int? = null
    ): Flow<PagingData<FamilyModel>>

    fun getPagingOrders(
        pageSize: Int,
        language: LanguageEnum,
        kingdomType: KingdomType,
        targetItemId: Int? = null
    ): Flow<PagingData<OrderModel>>

    fun getPagingHabitats(
        pageSize: Int,
        language: LanguageEnum,
        kingdomType: KingdomType,
        targetItemId: Int? = null
    ): Flow<PagingData<HabitatCategoryModel>>



    fun getLocalPagingClasses(
        label: String,
        pageSize: Int,
        language: LanguageEnum,
    ): Flow<PagingData<ClassModel>>

    fun getLocalPagingFamilies(
        label: String,
        pageSize: Int,
        language: LanguageEnum,
    ): Flow<PagingData<FamilyModel>>

    fun getLocalPagingOrders(
        label: String,
        pageSize: Int,
        language: LanguageEnum
    ): Flow<PagingData<OrderModel>>
}