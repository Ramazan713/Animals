package com.masterplus.animals.features.search.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.masterplus.animals.core.data.mapper.toCategoryData
import com.masterplus.animals.core.data.mapper.toClass
import com.masterplus.animals.core.data.mapper.toFamily
import com.masterplus.animals.core.data.mapper.toOrder
import com.masterplus.animals.core.data.mapper.toSpeciesDetail
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.domain.models.SpeciesDetail
import com.masterplus.animals.core.shared_features.database.dao.SearchCategoryDao
import com.masterplus.animals.core.shared_features.database.dao.SearchSpeciesDao
import com.masterplus.animals.features.search.domain.repo.SearchRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class SearchRepoImpl(
    private val searchCategoryDao: SearchCategoryDao,
    private val searchSpeciesDao: SearchSpeciesDao
): SearchRepo {
    override fun searchSpeciesWithCategory(
        query: String,
        categoryType: CategoryType
    ): Flow<PagingData<SpeciesDetail>> {
        val pagingConfig = PagingConfig(pageSize = 10)
        val queryCurrent = query.let { "%$it%" }.split(" ").joinToString("%")
        val queryOrder = query.let { "$it%" }.split(" ").joinToString("%")

        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
               searchSpeciesDao.searchPagingSpeciesTr(queryCurrent, queryOrder)
            }
        ).flow.map { items -> items.map { it.toSpeciesDetail() } }
    }

    override fun searchSpeciesWithCategory(
        query: String,
        categoryType: CategoryType,
        itemId: Int
    ): Flow<PagingData<SpeciesDetail>> {
        val pagingConfig = PagingConfig(pageSize = 10)
        val queryCurrent = query.let { "%$it%" }.split(" ").joinToString("%")
        val queryOrder = query.let { "$it%" }.split(" ").joinToString("%")

        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                when(categoryType){
                    CategoryType.Habitat -> searchSpeciesDao.searchPagingSpeciesTrByHabitatCategoryId(itemId, queryCurrent, queryOrder)
                    CategoryType.Class -> searchSpeciesDao.searchPagingSpeciesTrByClassId(itemId, queryCurrent, queryOrder)
                    CategoryType.Order -> searchSpeciesDao.searchPagingSpeciesTrByOrderId(itemId, queryCurrent, queryOrder)
                    CategoryType.Family -> searchSpeciesDao.searchPagingSpeciesTrByFamilyId(itemId, queryCurrent, queryOrder)
                    CategoryType.List -> searchSpeciesDao.searchPagingSpeciesTrByListId(itemId, queryCurrent, queryOrder)
                }
            }
        ).flow.map { items -> items.map { it.toSpeciesDetail() } }
    }


    override fun searchCategory(
        query: String,
        categoryType: CategoryType,
    ): Flow<PagingData<CategoryData>> {
        val pagingConfig = PagingConfig(pageSize = 10)
        val queryCurrent = query.let { "%$it%" }.split(" ").joinToString("%")
        val queryOrder = query.let { "$it%" }.split(" ").joinToString("%")

        return when(categoryType){
            CategoryType.Class -> {
                Pager(
                    config = pagingConfig,
                    pagingSourceFactory = {
                        searchCategoryDao.searchPagingClassesTr(queryCurrent, queryOrder)
                    }
                ).flow.map { items -> items.map { it.toClass().toCategoryData() } }
            }
            CategoryType.Order -> {
                Pager(
                    config = pagingConfig,
                    pagingSourceFactory = {
                        searchCategoryDao.searchOrdersTr(queryCurrent, queryOrder)
                    }
                ).flow.map { items -> items.map { it.toOrder().toCategoryData() } }
            }
            CategoryType.Family -> {
                Pager(
                    config = pagingConfig,
                    pagingSourceFactory = {
                        searchCategoryDao.searchFamiliesTr(queryCurrent, queryOrder)
                    }
                ).flow.map { items -> items.map { it.toFamily().toCategoryData() } }
            }
            else -> flowOf()
        }
    }

    override fun searchCategory(
        query: String,
        categoryType: CategoryType,
        itemId: Int
    ): Flow<PagingData<CategoryData>> {
        val pagingConfig = PagingConfig(pageSize = 10)
        val queryCurrent = query.let { "%$it%" }.split(" ").joinToString("%")
        val queryOrder = query.let { "$it%" }.split(" ").joinToString("%")

        return when (categoryType) {
            CategoryType.Class -> {
                Pager(
                    config = pagingConfig,
                    pagingSourceFactory = {
                        searchCategoryDao.searchOrdersTrWithClassId(queryCurrent, queryOrder, itemId)
                    }
                ).flow.map { items -> items.map { it.toOrder().toCategoryData() } }
            }

            CategoryType.Order -> {
                Pager(
                    config = pagingConfig,
                    pagingSourceFactory = {
                        searchCategoryDao.searchFamiliesTrWithOrderId(queryCurrent, queryOrder, itemId)
                    }
                ).flow.map { items -> items.map { it.toFamily().toCategoryData() } }
            }
            else -> flowOf()
        }
    }


}