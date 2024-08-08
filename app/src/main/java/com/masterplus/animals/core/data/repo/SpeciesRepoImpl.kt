package com.masterplus.animals.core.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.masterplus.animals.core.data.mapper.toSpeciesDetail
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.models.SpeciesDetail
import com.masterplus.animals.core.domain.repo.SpeciesRepo
import com.masterplus.animals.core.shared_features.database.dao.SpeciesDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SpeciesRepoImpl(
    private val speciesDao: SpeciesDao
): SpeciesRepo {
    
    override fun getPagingSpeciesList(
        categoryType: CategoryType,
        itemId: Int?,
        pageSize: Int
    ): Flow<PagingData<SpeciesDetail>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = {
                if(itemId == null){
                    return@Pager speciesDao.getPagingSpecies()
                }
                when(categoryType){
                    CategoryType.Habitat -> {
                        speciesDao.getPagingSpeciesByHabitatCategoryId(itemId)
                    }
                    CategoryType.Class -> {
                        speciesDao.getPagingSpeciesByClassId(itemId)
                    }
                    CategoryType.Order -> {
                        speciesDao.getPagingSpeciesByOrderId(itemId)
                    }
                    CategoryType.Family -> {
                        speciesDao.getPagingSpeciesByFamilyId(itemId)
                    }
                    CategoryType.List -> {
                        speciesDao.getPagingSpeciesByListId(itemId)
                    }
                }
            }
        ).flow.map { items ->
            items.map { it.toSpeciesDetail() }
        }
    }
}