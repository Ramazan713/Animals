package com.masterplus.animals.core.data.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.masterplus.animals.R
import com.masterplus.animals.core.data.datasources.CategoryRemoteSource
import com.masterplus.animals.core.data.mapper.toSpecies
import com.masterplus.animals.core.data.mapper.toSpeciesListDetail
import com.masterplus.animals.core.data.mediators.SpeciesCategoryRemoteMediator
import com.masterplus.animals.core.data.mediators.SpeciesKingdomRemoteMediator
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
    private val categoryRemoteSource: CategoryRemoteSource
): SpeciesRepo {
    
    @OptIn(ExperimentalPagingApi::class)
    override fun getPagingSpeciesList(
        categoryType: CategoryType,
        itemId: Int?,
        pageSize: Int,
        language: LanguageEnum,
        kingdom: KingdomType?
    ): Flow<PagingData<SpeciesListDetail>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = {
                val kingdomId = kingdom?.kingdomId ?: 0
                if(itemId == null){
                    return@Pager speciesDao.getPagingSpeciesByLabel(RemoteKeyUtil.getSpeciesKingdomRemoteKey(kingdom!!))
                }
                speciesDao.getPagingSpeciesByLabel(RemoteKeyUtil.getSpeciesCategoryRemoteKey(
                    categoryType = categoryType,
                    itemId = itemId
                ))
            },
            remoteMediator = when {
                itemId == null -> SpeciesKingdomRemoteMediator(
                    kingdom = kingdom ?: KingdomType.DEFAULT,
                    limit = pageSize,
                    db = db,
                    categoryRemoteSource = categoryRemoteSource
                )
                else -> SpeciesCategoryRemoteMediator(
                    categoryType = categoryType,
                    itemId = itemId ?: 0,
                    limit = pageSize,
                    db = db,
                    categoryRemoteSource = categoryRemoteSource
                )
            }
        ).flow.map { items ->
            items.map { it.toSpeciesListDetail(language) }
        }
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
                        val response = categoryRemoteSource.getHabitatsByIds(notExistsHabitatIds)
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
}