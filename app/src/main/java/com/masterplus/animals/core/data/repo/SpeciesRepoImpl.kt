package com.masterplus.animals.core.data.repo

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.masterplus.animals.core.data.datasources.CategoryRemoteSource
import com.masterplus.animals.core.data.mapper.toSpecies
import com.masterplus.animals.core.data.mapper.toSpeciesListDetail
import com.masterplus.animals.core.data.mediators.SpeciesCategoryRemoteMediator
import com.masterplus.animals.core.data.mediators.SpeciesKingdomRemoteMediator
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.SpeciesListDetail
import com.masterplus.animals.core.domain.models.SpeciesModel
import com.masterplus.animals.core.domain.repo.SpeciesRepo
import com.masterplus.animals.core.shared_features.database.AppDatabase
import com.masterplus.animals.core.shared_features.database.dao.SpeciesDao
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SpeciesRepoImpl(
    private val speciesDao: SpeciesDao,
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

//        return Pager(
//            config = PagingConfig(pageSize = pageSize),
//            pagingSourceFactory = {
//                val kingdomId = kingdom?.kingdomId ?: 0
//                speciesDao.getPagingSpecies(kingdomId)
//            },
//            remoteMediator = SpeciesCategoryRemoteMediator(
//                categoryType = categoryType,
//                itemId = itemId ?: 0,
//                limit = pageSize,
//                db = db,
//                categoryRemoteSource = categoryRemoteSource
//            )
//        ).flow.map { items ->
//            items.map { it.toSpeciesListDetail(language) }
//        }



        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = {
                val kingdomId = kingdom?.kingdomId ?: 0
                if(itemId == null){
                    return@Pager speciesDao.getPagingSpecies(kingdomId)
                }
                when(categoryType){
                    CategoryType.Habitat -> {
                        speciesDao.getPagingSpeciesByHabitatCategoryId(itemId, kingdomId)
                    }
                    CategoryType.Class -> {
                        speciesDao.getPagingSpeciesByClassId(itemId, kingdomId)
                    }
                    CategoryType.Order -> {
                        speciesDao.getPagingSpeciesByOrderId(itemId, kingdomId)
                    }
                    CategoryType.Family -> {
                        speciesDao.getPagingSpeciesByFamilyId(itemId, kingdomId)
                    }
                    CategoryType.List -> {
                        speciesDao.getPagingSpeciesByListId(itemId)
                    }
                }
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
}