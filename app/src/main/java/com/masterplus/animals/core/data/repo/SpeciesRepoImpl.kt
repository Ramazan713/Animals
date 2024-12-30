package com.masterplus.animals.core.data.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.masterplus.animals.R
import com.masterplus.animals.core.data.datasources.CategoryRemoteSource
import com.masterplus.animals.core.data.mapper.toClassWithImageEmbedded
import com.masterplus.animals.core.data.mapper.toFamilyWithImageEmbedded
import com.masterplus.animals.core.data.mapper.toHabitatCategoryWithImageEmbedded
import com.masterplus.animals.core.data.mapper.toOrderWithImageEmbedded
import com.masterplus.animals.core.data.mapper.toPhylumWithImageEmbedded
import com.masterplus.animals.core.data.mapper.toSpecies
import com.masterplus.animals.core.data.mapper.toSpeciesListDetail
import com.masterplus.animals.core.data.mediators.SpeciesCategoryRemoteMediator
import com.masterplus.animals.core.data.mediators.SpeciesKingdomRemoteMediator2
import com.masterplus.animals.core.data.mediators.SpeciesListRemoteMediator
import com.masterplus.animals.core.data.utils.RemoteKeyUtil
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.SpeciesListDetail
import com.masterplus.animals.core.domain.models.SpeciesModel
import com.masterplus.animals.core.domain.repo.SpeciesRepo
import com.masterplus.animals.core.domain.utils.EmptyDefaultResult
import com.masterplus.animals.core.domain.utils.EmptyResult
import com.masterplus.animals.core.domain.utils.ErrorText
import com.masterplus.animals.core.domain.utils.Result
import com.masterplus.animals.core.domain.utils.asEmptyResult
import com.masterplus.animals.core.domain.utils.map
import com.masterplus.animals.core.shared_features.analytics.domain.repo.ServerReadCounter
import com.masterplus.animals.core.shared_features.database.AppDatabase
import com.masterplus.animals.core.shared_features.database.dao.CategoryDao
import com.masterplus.animals.core.shared_features.database.dao.SpeciesDao
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class SpeciesRepoImpl(
    private val speciesDao: SpeciesDao,
    private val categoryDao: CategoryDao,
    private val db: AppDatabase,
    private val categoryRemoteSource: CategoryRemoteSource,
    private val serverReadCounter: ServerReadCounter
): SpeciesRepo {

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagingSpeciesWithKingdom(
        pageSize: Int,
        language: LanguageEnum,
        kingdom: KingdomType,
        targetItemId: Int?
    ): Flow<PagingData<SpeciesListDetail>> {
        return Pager(
            config = getPagingConfig(pageSize),
            pagingSourceFactory = {
                speciesDao.getPagingSpeciesByLabel(RemoteKeyUtil.getSpeciesKingdomRemoteKey(kingdom))
            },
            remoteMediator = SpeciesKingdomRemoteMediator2(
                kingdom = kingdom,
                db = db,
                categoryRemoteSource = categoryRemoteSource,
                targetItemId = targetItemId,
                serverReadCounter = serverReadCounter
            )
        ).flow.map { items ->
            items.map { it.toSpeciesListDetail(language) }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagingSpeciesWithList(
        itemId: Int,
        pageSize: Int,
        language: LanguageEnum,
        targetItemId: Int?
    ): Flow<PagingData<SpeciesListDetail>> {
        return Pager(
            config = getPagingConfig(pageSize),
            pagingSourceFactory = {
                speciesDao.getPagingSpeciesByListId(itemId)
            },
            remoteMediator = SpeciesListRemoteMediator(
                db = db,
                listId = itemId,
                targetItemId = targetItemId,
                readCounter = serverReadCounter,
                categoryRemoteSource = categoryRemoteSource
            )
        ).flow.map { items ->
            items.map { it.toSpeciesListDetail(language) }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagingSpeciesWithCategory(
        categoryType: CategoryType,
        itemId: Int,
        pageSize: Int,
        language: LanguageEnum,
        kingdom: KingdomType?,
        targetItemId: Int?
    ): Flow<PagingData<SpeciesListDetail>> {
        return Pager(
            config = getPagingConfig(pageSize),
            pagingSourceFactory = {
                speciesDao.getPagingSpeciesByLabel(RemoteKeyUtil.getSpeciesCategoryRemoteKey(
                    categoryType = categoryType,
                    itemId = itemId
                ))
            },
            remoteMediator = SpeciesCategoryRemoteMediator(
                categoryType = categoryType,
                itemId = itemId,
                db = db,
                targetItemId = targetItemId,
                readCounter = serverReadCounter,
                categoryRemoteSource = categoryRemoteSource
            )
        ).flow.map { items ->
            items.map { it.toSpeciesListDetail(language) }
        }
    }

    private fun getPagingConfig(pageSize: Int): PagingConfig{
        return PagingConfig(
            pageSize = pageSize,
            jumpThreshold = pageSize * 2
        )
    }

    override suspend fun getSpeciesById(speciesId: Int, lang: LanguageEnum): SpeciesModel? {
        return speciesDao.getSpeciesById(speciesId)?.toSpecies(lang)
    }

    override suspend fun checkSpeciesDetailData(species: SpeciesModel): EmptyDefaultResult {
        return coroutineScope {
            val tasks: List<Deferred<EmptyResult<ErrorText>>> = listOf(
                async {
                    if (categoryDao.getPhylumWithId2(species.phylumId) == null) {
                        val response = categoryRemoteSource.getPhylumById(species.phylumId)
                            .map { it?.toPhylumWithImageEmbedded(RemoteKeyUtil.getPhylumRemoteKey(species.kingdomType)) }
                        response.onSuccessAsync {
                            categoryDao.insertPhylumWithImages(listOf(it!!))
                        }
                        return@async response.asEmptyResult()
                    }
                    Result.Success(Unit)
                },
                async {
                    if (categoryDao.getClassWithId2(species.classId) == null) {
                        val response = categoryRemoteSource.getClassById(species.classId)
                            .map { it?.toClassWithImageEmbedded(RemoteKeyUtil.getClassRemoteKey(kingdomType = species.kingdomType, phylumId = species.phylumId)) }
                        response.onSuccessAsync {
                            categoryDao.insertClassesWithImages(listOf(it!!))
                        }
                        return@async response.asEmptyResult()
                    }
                    Result.Success(Unit)
                },
                async {
                    if (categoryDao.getOrderWithId2(species.orderId) == null) {
                        val response = categoryRemoteSource.getOrderById(species.orderId)
                            .map { it?.toOrderWithImageEmbedded(RemoteKeyUtil.getOrderRemoteKey(kingdomType = species.kingdomType, classId = species.classId))
                            }
                        response.onSuccessAsync {
                            categoryDao.insertOrdersWithImages(listOf(it!!))
                        }
                        return@async response.asEmptyResult()
                    }
                    Result.Success(Unit)
                },
                async {
                    if (categoryDao.getFamilyWithId2(species.familyId) == null) {
                        val response = categoryRemoteSource.getFamilyById(species.familyId)
                            .map { it?.toFamilyWithImageEmbedded(RemoteKeyUtil.getFamilyRemoteKey(kingdomType = species.kingdomType, orderId = species.orderId)) }
                        response.onSuccessAsync {
                            categoryDao.insertFamiliesWithImages(listOf(it!!))
                        }
                        return@async response.asEmptyResult()
                    }
                    Result.Success(Unit)
                },
                async {
                    val notExistsHabitatIds = speciesDao.getNotExistsHabitatIds(species.id)
                    if (notExistsHabitatIds.isNotEmpty()) {
                        val response = categoryRemoteSource.getHabitatsByIds(notExistsHabitatIds, loadKey = null, limit = notExistsHabitatIds.size)
                            .map { items -> items.map { it.toHabitatCategoryWithImageEmbedded(RemoteKeyUtil.getHabitatRemoteKey(species.kingdomType)) } }
                        response.getSuccessData?.let { data ->
                            categoryDao.insertHabitatsWithImages(data)
                            if(data.size != notExistsHabitatIds.size){
                                return@async Result.errorWithResource(R.string.something_went_wrong)
                            }
                        }
                        return@async response.asEmptyResult()
                    }
                    Result.Success(Unit)
                }
            )
            val results = tasks.awaitAll()
            val error = results.firstNotNullOfOrNull { it.getFailureError?.text }
            if(error != null) return@coroutineScope Result.errorWithUiText(error)
            return@coroutineScope Result.Success(Unit)
        }

    }

    override suspend fun getSpeciesPosByLabel(itemId: Int, label: String): Int? {
        speciesDao.getSpeciesByIdAndLabel(itemId, label) ?: return null
        return speciesDao.getSpeciesPosByLabel(itemId, label)
    }
}