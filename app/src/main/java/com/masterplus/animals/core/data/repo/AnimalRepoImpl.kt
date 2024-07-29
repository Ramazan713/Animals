package com.masterplus.animals.core.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.masterplus.animals.core.data.mapper.toAnimal
import com.masterplus.animals.core.data.mapper.toAnimalData
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.models.Animal
import com.masterplus.animals.core.domain.models.AnimalData
import com.masterplus.animals.core.domain.repo.AnimalRepo
import com.masterplus.animals.core.shared_features.database.dao.AnimalDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AnimalRepoImpl constructor(
    private val animalDao: AnimalDao
): AnimalRepo {

    override suspend fun getAnimalById(animalId: Int): Animal? {
       return animalDao.getAnimalById(animalId)?.toAnimal()
    }

    override fun getPagingAnimalsList(
        categoryType: CategoryType,
        itemId: Int?,
        pageSize: Int
    ): Flow<PagingData<AnimalData>> {
        return Pager(
            config = PagingConfig(pageSize = pageSize),
            pagingSourceFactory = {
                if(itemId == null){
                    return@Pager animalDao.getPagingAnimals()
                }
                when(categoryType){
                    CategoryType.Habitat -> {
                        animalDao.getPagingAnimalsByHabitatCategoryId(itemId)
                    }
                    CategoryType.Class -> {
                        animalDao.getPagingAnimalsByClassId(itemId)
                    }
                    CategoryType.Order -> {
                        animalDao.getPagingAnimalsByOrderId(itemId)
                    }
                    CategoryType.Family -> {
                        animalDao.getPagingAnimalsByFamilyId(itemId)
                    }
                }
            }
        ).flow.map { items ->
            items.map { it.toAnimalData() }
        }
    }
}