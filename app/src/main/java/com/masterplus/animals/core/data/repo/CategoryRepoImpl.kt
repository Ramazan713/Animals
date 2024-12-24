package com.masterplus.animals.core.data.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.masterplus.animals.core.data.datasources.CategoryRemoteSource
import com.masterplus.animals.core.data.mapper.toCategoryData
import com.masterplus.animals.core.data.mapper.toClass
import com.masterplus.animals.core.data.mapper.toFamily
import com.masterplus.animals.core.data.mapper.toHabitatCategory
import com.masterplus.animals.core.data.mapper.toOrder
import com.masterplus.animals.core.data.mediators.ClassRemoteMediator
import com.masterplus.animals.core.data.mediators.FamilyRemoteMediator
import com.masterplus.animals.core.data.mediators.HabitatRemoteMediator
import com.masterplus.animals.core.data.mediators.OrderRemoteMediator
import com.masterplus.animals.core.data.utils.RemoteKeyUtil
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.domain.models.ClassModel
import com.masterplus.animals.core.domain.models.FamilyModel
import com.masterplus.animals.core.domain.models.HabitatCategoryModel
import com.masterplus.animals.core.domain.models.OrderModel
import com.masterplus.animals.core.domain.repo.CategoryRemoteRepo
import com.masterplus.animals.core.domain.repo.CategoryRepo
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.domain.utils.EmptyDefaultResult
import com.masterplus.animals.core.domain.utils.Result
import com.masterplus.animals.core.domain.utils.map
import com.masterplus.animals.core.shared_features.database.AppDatabase
import com.masterplus.animals.core.shared_features.database.dao.CategoryDao
import com.masterplus.animals.core.shared_features.database.dao.ListDao
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryRepoImpl constructor(
    private val categoryDao: CategoryDao,
    private val listDao: ListDao,
    private val db: AppDatabase,
    private val categoryRemoteRepo: CategoryRemoteRepo,
    private val categoryRemoteSource: CategoryRemoteSource
): CategoryRepo {
    override suspend fun getCategoryData(
        categoryType: CategoryType,
        limit: Int,
        language: LanguageEnum,
        kingdomType: KingdomType
    ): DefaultResult<List<CategoryData>> {
        return when(categoryType){
            CategoryType.Habitat -> {
                fetchWithFallback(
                    localFetch = { categoryDao.getHabitatCategories(limit, RemoteKeyUtil.getHabitatRemoteKey(kingdomType)) },
                    remoteFetch = {
                        categoryRemoteRepo.getHabitats(kingdomType = kingdomType, limit = limit)
                    },
                    mapToData = { it.toHabitatCategory(language).toCategoryData() }
                )
            }
            CategoryType.Class -> {
                fetchWithFallback(
                    localFetch = { categoryDao.getClasses(limit, RemoteKeyUtil.getClassRemoteKey(kingdomType, null)) },
                    remoteFetch = {
                        categoryRemoteRepo.getClasses(kingdomType = kingdomType, limit = limit,)
                    },
                    mapToData = { it.toClass(language).toCategoryData() }
                )
            }
            CategoryType.Order -> {
                fetchWithFallback(
                    localFetch = { categoryDao.getOrders(limit, RemoteKeyUtil.getOrderRemoteKey(kingdomType, null)) },
                    remoteFetch = {
                        categoryRemoteRepo.getOrders(kingdomType = kingdomType, limit = limit,)
                    },
                    mapToData = { it.toOrder(language).toCategoryData() }
                )
            }
            CategoryType.Family -> {
                fetchWithFallback(
                    localFetch = { categoryDao.getFamilies(limit, RemoteKeyUtil.getFamilyRemoteKey(kingdomType, null)) },
                    remoteFetch = {
                        categoryRemoteRepo.getFamilies(kingdomType = kingdomType, limit = limit,)
                    },
                    mapToData = { it.toFamily(language).toCategoryData() }
                )
            }
            CategoryType.List -> Result.Success(emptyList())
        }
    }

    private suspend fun <T> fetchWithFallback(
        localFetch: suspend () -> List<T>,
        remoteFetch: suspend () -> EmptyDefaultResult,
        mapToData: (T) -> CategoryData
    ): DefaultResult<List<CategoryData>> {
        val localData = localFetch()
        if (localData.isNotEmpty()) {
            return Result.Success(localData.map(mapToData))
        }

        val remoteResult = remoteFetch()
        if (remoteResult.isError) {
            return remoteResult.map { emptyList() }
        }
        return Result.Success(localFetch().map(mapToData))
    }

    override suspend fun getCategoryName(categoryType: CategoryType, itemId: Int, language: LanguageEnum): String? {
        val title = when(categoryType){
            CategoryType.Habitat -> categoryDao.getHabitatCategoryWithId(itemId)?.habitat?.let { if(language.isEn) it.habitat_category_en else it.habitat_category_tr }
            CategoryType.Class -> categoryDao.getClassWithId(itemId)?.classEntity?.scientific_name
            CategoryType.Order -> categoryDao.getOrderWithId(itemId)?.order?.scientific_name
            CategoryType.Family -> categoryDao.getFamilyWithId(itemId)?.family?.scientific_name
            CategoryType.List -> listDao.getListById(itemId)?.name
        }
        return title
    }


    override suspend fun getClassWithId(classId: Int, language: LanguageEnum): ClassModel? {
        return categoryDao.getClassWithId(classId)?.toClass(language)
    }


    override suspend fun getOrderWithId(orderId: Int, language: LanguageEnum): OrderModel? {
        return categoryDao.getOrderWithId(orderId)?.toOrder(language)
    }

    override suspend fun getFamilyWithId(familyId: Int, language: LanguageEnum): FamilyModel? {
        return categoryDao.getFamilyWithId(familyId)?.toFamily(language)
    }


    @OptIn(ExperimentalPagingApi::class)
    override fun getPagingOrdersWithClassId(
        classId: Int,
        pageSize: Int,
        language: LanguageEnum,
        kingdomType: KingdomType
    ): Flow<PagingData<OrderModel>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = { categoryDao.getPagingOrders(RemoteKeyUtil.getOrderRemoteKey(kingdomType, classId)) },
            remoteMediator = OrderRemoteMediator(
                db = db,
                kingdomType = kingdomType,
                classId = classId,
                categoryRemoteSource = categoryRemoteSource,
                limit = pageSize
            )
        ).flow.map { items -> items.map { it.toOrder(language) } }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagingFamiliesWithOrderId(
        orderId: Int,
        pageSize: Int,
        language: LanguageEnum,
        kingdomType: KingdomType
    ): Flow<PagingData<FamilyModel>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = { categoryDao.getPagingFamilies(RemoteKeyUtil.getFamilyRemoteKey(kingdomType, orderId)) },
            remoteMediator = FamilyRemoteMediator(
                db = db,
                kingdomType = kingdomType,
                orderId = orderId,
                categoryRemoteSource = categoryRemoteSource,
                limit = pageSize
            )
        ).flow.map { items -> items.map { it.toFamily(language) } }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagingClasses(pageSize: Int, language: LanguageEnum, kingdomType: KingdomType): Flow<PagingData<ClassModel>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = { categoryDao.getPagingClasses(RemoteKeyUtil.getClassRemoteKey(kingdomType, null)) },
            remoteMediator = ClassRemoteMediator(
                db = db,
                kingdomType = kingdomType,
                phylumId = null,
                categoryRemoteSource = categoryRemoteSource,
                limit = pageSize
            )
        ).flow.map { items -> items.map { it.toClass(language) } }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagingOrders(pageSize: Int, language: LanguageEnum, kingdomType: KingdomType): Flow<PagingData<OrderModel>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = { categoryDao.getPagingOrders(RemoteKeyUtil.getOrderRemoteKey(kingdomType, null)) },
            remoteMediator = OrderRemoteMediator(
                db = db,
                kingdomType = kingdomType,
                classId = null,
                categoryRemoteSource = categoryRemoteSource,
                limit = pageSize
            )
        ).flow.map { items -> items.map { it.toOrder(language) } }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagingHabitats(
        pageSize: Int,
        language: LanguageEnum,
        kingdomType: KingdomType
    ): Flow<PagingData<HabitatCategoryModel>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = { categoryDao.getPagingHabitats(RemoteKeyUtil.getHabitatRemoteKey(kingdomType)) },
            remoteMediator = HabitatRemoteMediator(
                db = db,
                kingdomType = kingdomType,
                categoryRemoteSource = categoryRemoteSource,
                limit = pageSize
            )
        ).flow.map { items -> items.map { it.toHabitatCategory(language) } }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagingFamilies(pageSize: Int, language: LanguageEnum, kingdomType: KingdomType): Flow<PagingData<FamilyModel>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = { categoryDao.getPagingFamilies(RemoteKeyUtil.getFamilyRemoteKey(kingdomType, null)) },
            remoteMediator = FamilyRemoteMediator(
                db = db,
                kingdomType = kingdomType,
                orderId = null,
                categoryRemoteSource = categoryRemoteSource,
                limit = pageSize
            )
        ).flow.map { items -> items.map { it.toFamily(language) } }
    }
}