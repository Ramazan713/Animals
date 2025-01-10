package com.masterplus.animals.features.search.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.masterplus.animals.R
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
import com.masterplus.animals.core.domain.repo.ConnectivityObserver
import com.masterplus.animals.core.domain.utils.DefaultResult
import com.masterplus.animals.core.domain.utils.EmptyDefaultResult
import com.masterplus.animals.core.domain.utils.Result
import com.masterplus.animals.core.domain.utils.asEmptyResult
import com.masterplus.animals.core.domain.utils.map
import com.masterplus.animals.core.shared_features.database.AppDatabase
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import com.masterplus.animals.features.search.domain.repo.SearchRemoteRepo
import com.masterplus.animals.features.search.domain.service.SearchSpeciesIndexService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class SearchRemoteRepoImpl(
    private val categoryRemoteSource: CategoryRemoteSource,
    private val searchSpeciesIndexService: SearchSpeciesIndexService,
    private val insertSpeciesHelper: InsertFirebaseSpeciesHelper,
    private val connectivityObserver: ConnectivityObserver,
    private val db: AppDatabase
): SearchRemoteRepo {

    override suspend fun searchAll(
        query: String,
        responsePageSize: Int,
        languageEnum: LanguageEnum
    ): EmptyDefaultResult {
        if(!connectivityObserver.hasConnection()){
            return Result.errorWithResource(R.string.check_your_connection)
        }
        val indexResponse = searchSpeciesIndexService.searchAll(query = query, pageSize = responsePageSize, languageEnum = languageEnum)
        val successSearchResults = indexResponse.getSuccessData ?: return indexResponse.asEmptyResult()
        return coroutineScope {
            val allJobs = successSearchResults.map { searchResult ->
                async<EmptyDefaultResult> {
                    val indexIds = searchResult.ids.mapNotNull { it.toIntOrNull() }
                    if(indexIds.isEmpty()) return@async Result.Success(Unit)
                    val label = RemoteKeyUtil.getAppSearchKey(query)
                    when(searchResult.type){
                        SearchSpeciesIndexService.Companion.SearchAllResultType.Species -> {
                            insertSpecies(indexIds = indexIds, label = label)
                        }
                        SearchSpeciesIndexService.Companion.SearchAllResultType.Classes -> {
                            insertClasses(indexIds = indexIds, label = label)
                        }
                        SearchSpeciesIndexService.Companion.SearchAllResultType.Orders -> {
                            insertOrders(indexIds = indexIds, label = label)
                        }
                        SearchSpeciesIndexService.Companion.SearchAllResultType.Families -> {
                            insertFamilies(indexIds = indexIds, label = label)
                        }
                    }
                }
            }.awaitAll()
            return@coroutineScope allJobs.find { it.getFailureError != null } ?: Result.Success(Unit)
        }

    }

    override suspend fun searchSpeciesWithCategory(
        query: String,
        categoryItemId: Int?,
        localPageSize: Int,
        responsePageSize: Int,
        categoryType: CategoryType,
        language: LanguageEnum,
        kingdomType: KingdomType
    ): DefaultResult<Flow<PagingData<SpeciesListDetail>>> {
        if(!connectivityObserver.hasConnection()){
            return Result.errorWithResource(R.string.check_your_connection)
        }
        val label = RemoteKeyUtil.getSpeciesCategorySearchKey(
            query = query,
            categoryType = categoryType,
            itemId = categoryItemId,
            kingdomType = kingdomType
        )

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

        val insertSpeciesResponse = insertSpecies(indexIds = indexIds, label = label)
        if(insertSpeciesResponse.isError) return insertSpeciesResponse.map { flowOf() }

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
        if(!connectivityObserver.hasConnection()){
            return Result.errorWithResource(R.string.check_your_connection)
        }
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

        val result = when(categoryType){
            CategoryType.Habitat -> Result.Success(Unit)
            CategoryType.Class -> {
                insertClasses(indexIds = indexIds, label = label)
            }
            CategoryType.Order -> {
                insertOrders(indexIds = indexIds, label = label)
            }
            CategoryType.Family -> {
                insertFamilies(indexIds = indexIds, label = label)
            }
            CategoryType.List -> Result.Success(Unit)
        }
        if(result.isError) return result.map { flowOf() }

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
            pagingSourceFactory = { db.speciesDao.getPagingSpeciesByLabel(label) }
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

    private suspend fun insertSpecies(indexIds: List<Int>, label: String): EmptyDefaultResult{
        val speciesResponse = categoryRemoteSource.getSpecies(
            itemIds = indexIds,
            loadKey = null,
            limit = indexIds.size,
            incrementCounter = false,
            orderByKey = null,
        )
        val speciesData = speciesResponse.getSuccessData?.let { data ->
            data.map { it.copy(order_key = indexIds.indexOf(it.id)) }
        } ?: return speciesResponse.asEmptyResult()
        insertSpeciesHelper.insertSpecies(speciesData, label)
        return speciesResponse.asEmptyResult()
    }

    private suspend fun insertClasses(indexIds: List<Int>, label: String): EmptyDefaultResult{
        val itemsResponse = categoryRemoteSource.getClasses(
            itemIds = indexIds,
            orderByKey = null,
            incrementCounter = false
        )
        val itemsData = itemsResponse.getSuccessData?.let { data ->
            data.map { it.copy(order_key = indexIds.indexOf(it.id)) }
        } ?: return itemsResponse.asEmptyResult()
        db.categoryDao.insertClassesWithImages(itemsData.map { it.toClassWithImageEmbedded(label) })
        return itemsResponse.asEmptyResult()
    }

    private suspend fun insertOrders(indexIds: List<Int>, label: String): EmptyDefaultResult{
        val itemsResponse = categoryRemoteSource.getOrders(
            itemIds = indexIds,
            orderByKey = null,
            incrementCounter = false
        )
        val itemsData = itemsResponse.getSuccessData?.let { data ->
            data.map { it.copy(order_key = indexIds.indexOf(it.id)) }
        } ?: return itemsResponse.asEmptyResult()
        db.categoryDao.insertOrdersWithImages(itemsData.map { it.toOrderWithImageEmbedded(label) })
        return itemsResponse.asEmptyResult()
    }

    private suspend fun insertFamilies(indexIds: List<Int>, label: String): EmptyDefaultResult{
        val itemsResponse = categoryRemoteSource.getFamilies(
            itemIds = indexIds,
            orderByKey = null,
            incrementCounter = false
        )
        val itemsData = itemsResponse.getSuccessData?.let { data ->
            data.map { it.copy(order_key = indexIds.indexOf(it.id)) }
        } ?: return itemsResponse.asEmptyResult()
        db.categoryDao.insertFamiliesWithImages(itemsData.map { it.toFamilyWithImageEmbedded(label) })
        return itemsResponse.asEmptyResult()
    }
}