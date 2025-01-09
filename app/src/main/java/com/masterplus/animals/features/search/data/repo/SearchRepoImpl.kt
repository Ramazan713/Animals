package com.masterplus.animals.features.search.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.masterplus.animals.core.data.mapper.toCategoryData
import com.masterplus.animals.core.data.mapper.toClass
import com.masterplus.animals.core.data.mapper.toFamily
import com.masterplus.animals.core.data.mapper.toOrder
import com.masterplus.animals.core.data.mapper.toSpeciesListDetail
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.domain.models.SpeciesListDetail
import com.masterplus.animals.core.shared_features.database.dao.SearchCategoryDao
import com.masterplus.animals.core.shared_features.database.dao.SearchSpeciesDao
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import com.masterplus.animals.features.search.domain.repo.SearchRepo
import com.masterplus.animals.features.search.domain.use_cases.GetSearchQueryUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class SearchRepoImpl(
    private val searchCategoryDao: SearchCategoryDao,
    private val searchSpeciesDao: SearchSpeciesDao,
    private val getQueryUseCase: GetSearchQueryUseCase,
): SearchRepo {

    override fun searchSpecies(
        query: String,
        pageSize: Int,
        language: LanguageEnum
    ): Flow<PagingData<SpeciesListDetail>> {
        val pagingConfig = PagingConfig(pageSize = pageSize)
        val queryResult = getQueryUseCase(query)

        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                if(language.isEn){
                    searchSpeciesDao.searchPagingSpeciesEn(queryResult.queryInLike, queryResult.queryForOrder)
                }else{
                    searchSpeciesDao.searchPagingSpeciesTr(queryResult.queryInLike, queryResult.queryForOrder)
                }
            }
        ).flow.map { items -> items.map { it.toSpeciesListDetail(language) } }
    }

    override fun searchSpeciesWithCategory(
        query: String,
        pageSize: Int,
        categoryType: CategoryType,
        kingdomType: KingdomType,
        itemId: Int,
        language: LanguageEnum
    ): Flow<PagingData<SpeciesListDetail>> {
        val pagingConfig = PagingConfig(pageSize = pageSize)
        val queryResult = getQueryUseCase(query)
        val queryInLike = queryResult.queryInLike
        val queryForOrder = queryResult.queryForOrder

        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                when(categoryType){
                    CategoryType.Habitat -> {
                        //TODO: Add kingdomType for searching Habitats
                        if(language.isEn) searchSpeciesDao.searchPagingSpeciesEnByHabitatCategoryId(itemId, queryInLike, queryForOrder) else
                            searchSpeciesDao.searchPagingSpeciesTrByHabitatCategoryId(itemId, queryInLike, queryForOrder)
                    }
                    CategoryType.Class -> {
                        if(language.isEn) searchSpeciesDao.searchPagingSpeciesEnByClassId(itemId, queryInLike, queryForOrder) else
                            searchSpeciesDao.searchPagingSpeciesTrByClassId(itemId, queryInLike, queryForOrder)
                    }
                    CategoryType.Order -> {
                        if(language.isEn) searchSpeciesDao.searchPagingSpeciesEnByOrderId(itemId, queryInLike, queryForOrder) else
                            searchSpeciesDao.searchPagingSpeciesTrByOrderId(itemId, queryInLike, queryForOrder)
                    }
                    CategoryType.Family -> {
                        if(language.isEn) searchSpeciesDao.searchPagingSpeciesEnByFamilyId(itemId, queryInLike, queryForOrder) else
                            searchSpeciesDao.searchPagingSpeciesTrByFamilyId(itemId, queryInLike, queryForOrder)
                    }
                    CategoryType.List -> {
                        if(language.isEn) searchSpeciesDao.searchPagingSpeciesEnByListId(itemId, queryInLike, queryForOrder) else
                            searchSpeciesDao.searchPagingSpeciesTrByListId(itemId, queryInLike, queryForOrder)
                    }
                }
            }
        ).flow.map { items -> items.map { it.toSpeciesListDetail(language) } }
    }


    override fun searchCategory(
        query: String,
        pageSize: Int,
        categoryType: CategoryType,
        language: LanguageEnum
    ): Flow<PagingData<CategoryData>> {
        val pagingConfig = PagingConfig(pageSize = pageSize)
        val queryResult = getQueryUseCase(query)
        val queryInLike = queryResult.queryInLike
        val queryForOrder = queryResult.queryForOrder

        return when(categoryType){
            CategoryType.Class -> {
                Pager(
                    config = pagingConfig,
                    pagingSourceFactory = {
                        if(language.isEn) searchCategoryDao.searchPagingClassesEn(queryInLike, queryForOrder) else
                            searchCategoryDao.searchPagingClassesTr(queryInLike, queryForOrder)
                    }
                ).flow.map { items -> items.map { it.toClass(language).toCategoryData() } }
            }
            CategoryType.Order -> {
                Pager(
                    config = pagingConfig,
                    pagingSourceFactory = {
                        if(language.isEn) searchCategoryDao.searchOrdersEn(queryInLike, queryForOrder) else
                            searchCategoryDao.searchOrdersTr(queryInLike, queryForOrder)
                    }
                ).flow.map { items -> items.map { it.toOrder(language).toCategoryData() } }
            }
            CategoryType.Family -> {
                Pager(
                    config = pagingConfig,
                    pagingSourceFactory = {
                        if(language.isEn) searchCategoryDao.searchFamiliesEn(queryInLike, queryForOrder) else
                            searchCategoryDao.searchFamiliesTr(queryInLike, queryForOrder)
                    }
                ).flow.map { items -> items.map { it.toFamily(language).toCategoryData() } }
            }
            else -> flowOf()
        }
    }

    override fun searchCategory(
        query: String,
        pageSize: Int,
        categoryType: CategoryType,
        itemId: Int,
        language: LanguageEnum
    ): Flow<PagingData<CategoryData>> {
        val pagingConfig = PagingConfig(pageSize = pageSize)
        val queryResult = getQueryUseCase(query)
        val queryInLike = queryResult.queryInLike
        val queryForOrder = queryResult.queryForOrder

        return when (categoryType) {
            CategoryType.Class -> {
                Pager(
                    config = pagingConfig,
                    pagingSourceFactory = {
                        if(language.isEn) searchCategoryDao.searchOrdersEnWithClassId(queryInLike, queryForOrder, itemId) else
                            searchCategoryDao.searchOrdersTrWithClassId(queryInLike, queryForOrder, itemId)
                    }
                ).flow.map { items -> items.map { it.toOrder(language).toCategoryData() } }
            }

            CategoryType.Order -> {
                Pager(
                    config = pagingConfig,
                    pagingSourceFactory = {
                        if(language.isEn) searchCategoryDao.searchFamiliesEnWithOrderId(queryInLike, queryForOrder, itemId) else
                            searchCategoryDao.searchFamiliesTrWithOrderId(queryInLike, queryForOrder, itemId)
                    }
                ).flow.map { items -> items.map { it.toFamily(language).toCategoryData() } }
            }
            else -> flowOf()
        }
    }


}