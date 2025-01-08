package com.masterplus.animals.features.search.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.masterplus.animals.core.data.datasources.CategoryRemoteSource
import com.masterplus.animals.core.data.helpers.InsertFirebaseSpeciesHelper
import com.masterplus.animals.core.data.mapper.toCategoryData
import com.masterplus.animals.core.data.mapper.toClass
import com.masterplus.animals.core.data.mapper.toClassWithImageEmbedded
import com.masterplus.animals.core.data.mapper.toFamily
import com.masterplus.animals.core.data.mapper.toFamilyWithImageEmbedded
import com.masterplus.animals.core.data.mapper.toOrder
import com.masterplus.animals.core.data.mapper.toOrderWithImageEmbedded
import com.masterplus.animals.core.data.mapper.toSpeciesListDetail
import com.masterplus.animals.core.data.utils.RemoteKeyUtil
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.domain.models.SpeciesListDetail
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.domain.utils.Result
import com.masterplus.animals.core.domain.utils.map
import com.masterplus.animals.core.shared_features.database.AppDatabase
import com.masterplus.animals.core.shared_features.database.dao.SpeciesDao
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import com.masterplus.animals.features.search.domain.repo.SearchRemoteRepo
import com.masterplus.animals.features.search.domain.service.SearchSpeciesIndexService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class SearchRemoteRepoImpl(
    private val categoryRemoteSource: CategoryRemoteSource,
    private val searchSpeciesIndexService: SearchSpeciesIndexService,
    private val insertSpeciesHelper: InsertFirebaseSpeciesHelper,
    private val speciesDao: SpeciesDao,
    private val db: AppDatabase
): SearchRemoteRepo {

    override suspend fun searchSpeciesWithCategory(
        query: String,
        categoryItemId: Int?,
        localPageSize: Int,
        responsePageSize: Int,
        categoryType: CategoryType,
        language: LanguageEnum,
        kingdomType: KingdomType
    ): DefaultResult<Flow<PagingData<SpeciesListDetail>>> {
        val label = RemoteKeyUtil.getSpeciesCategorySearchKey(query = query, categoryType = categoryType, itemId = categoryItemId)

        val indexResponse = searchSpeciesIndexService.searchSpecies(
            query = query,
            pageSize = responsePageSize,
            kingdomType = kingdomType,
            languageEnum = language,
            categoryType = categoryType,
            categoryItemId = categoryItemId
        )
        val indexIds = indexResponse.getSuccessData?.mapNotNull { it.toIntOrNull() } ?: return indexResponse.map { emptyFlow() }
        if(indexIds.isEmpty()) return getSpeciesWithCategoriesPagingFlow(label, localPageSize, language)
        val speciesResponse = categoryRemoteSource.getSpecies(
            itemIds = indexIds,
            loadKey = null,
            limit = responsePageSize,
            incrementCounter = false,
            orderByKey = null,
        )
        val speciesData = speciesResponse.getSuccessData?.let { data ->
            data.map { it.copy(order_key = indexIds.indexOf(it.id)) }
        } ?: return speciesResponse.map { emptyFlow() }
        insertSpeciesHelper.insertSpecies(speciesData, label)

        return getSpeciesWithCategoriesPagingFlow(
            label, localPageSize, language
        )
    }

    override suspend fun searchCategories(
        query: String,
        parentItemId: Int?,
        localPageSize: Int,
        responsePageSize: Int,
        categoryType: CategoryType,
        language: LanguageEnum,
        kingdomType: KingdomType
    ): DefaultResult<Flow<PagingData<CategoryData>>> {
        val label = RemoteKeyUtil.getRemoteKeyWithCategoryTypeSearchKey(
            query = query,
            categoryType = categoryType,
            parentItemId = parentItemId,
            kingdomType = kingdomType
        )

        val indexResponse = searchSpeciesIndexService.searchCategories(
            query = query,
            pageSize = responsePageSize,
            kingdomType = kingdomType,
            languageEnum = language,
            categoryType = categoryType,
            parentItemId = parentItemId
        )
        val indexIds = indexResponse.getSuccessData?.mapNotNull { it.toIntOrNull() } ?: return indexResponse.map { emptyFlow() }
        if(indexIds.isEmpty()) return getCategoriesPagingFlow(label, localPageSize, language, categoryType)

        when(categoryType){
            CategoryType.Habitat -> Unit
            CategoryType.Class -> {
                val itemsResponse = categoryRemoteSource.getClasses(
                    itemIds = indexIds,
                    orderByKey = null,
                    incrementCounter = false
                )
                val itemsData = itemsResponse.getSuccessData?.let { data ->
                    data.map { it.copy(order_key = indexIds.indexOf(it.id)) }
                } ?: return itemsResponse.map { emptyFlow() }
                db.categoryDao.insertClassesWithImages(itemsData.map { it.toClassWithImageEmbedded(label) })
            }
            CategoryType.Order -> {
                val itemsResponse = categoryRemoteSource.getOrders(
                    itemIds = indexIds,
                    orderByKey = null,
                    incrementCounter = false
                )
                val itemsData = itemsResponse.getSuccessData?.let { data ->
                    data.map { it.copy(order_key = indexIds.indexOf(it.id)) }
                } ?: return itemsResponse.map { emptyFlow() }
                db.categoryDao.insertOrdersWithImages(itemsData.map { it.toOrderWithImageEmbedded(label) })
            }
            CategoryType.Family -> {
                val itemsResponse = categoryRemoteSource.getFamilies(
                    itemIds = indexIds,
                    orderByKey = null,
                    incrementCounter = false
                )
                val itemsData = itemsResponse.getSuccessData?.let { data ->
                    data.map { it.copy(order_key = indexIds.indexOf(it.id)) }
                } ?: return itemsResponse.map { emptyFlow() }
                db.categoryDao.insertFamiliesWithImages(itemsData.map { it.toFamilyWithImageEmbedded(label) })
            }
            CategoryType.List -> Unit
        }

        return getCategoriesPagingFlow(
            label = label,
            localPageSize = localPageSize,
            language = language,
            categoryType = categoryType
        )
    }



    private fun getSpeciesWithCategoriesPagingFlow(
        label: String,
        localPageSize: Int,
        language: LanguageEnum
    ): DefaultResult<Flow<PagingData<SpeciesListDetail>>>{
        val flowData = Pager(
            config = PagingConfig(pageSize = localPageSize),
            pagingSourceFactory = { speciesDao.getPagingSpeciesByLabel(label) }
        ).flow.map { items -> items.map { it.toSpeciesListDetail(language) } }
        return Result.Success(flowData)
    }

    private fun getCategoriesPagingFlow(
        label: String,
        localPageSize: Int,
        language: LanguageEnum,
        categoryType: CategoryType
    ): DefaultResult<Flow<PagingData<CategoryData>>>{
        val pagingConfig = PagingConfig(pageSize = localPageSize)
        val flowData = when(categoryType){
            CategoryType.Class -> {
                Pager(
                    config = pagingConfig,
                    pagingSourceFactory = { db.categoryDao.getPagingClasses(label) }
                ).flow.map { items -> items.map { it.toClass(language).toCategoryData() } }
            }
            CategoryType.Order -> {
                Pager(
                    config = pagingConfig,
                    pagingSourceFactory = { db.categoryDao.getPagingOrders(label) }
                ).flow.map { items -> items.map { it.toOrder(language).toCategoryData() } }
            }
            CategoryType.Family -> {
                Pager(
                    config = pagingConfig,
                    pagingSourceFactory = { db.categoryDao.getPagingFamilies(label) }
                ).flow.map { items -> items.map { it.toFamily(language).toCategoryData() } }
            }
            else -> flowOf()
        }
        return Result.Success(flowData)
    }
}