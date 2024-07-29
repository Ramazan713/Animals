package com.masterplus.animals.core.domain.repo

import androidx.paging.PagingData
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.models.Animal
import com.masterplus.animals.core.domain.models.AnimalData
import kotlinx.coroutines.flow.Flow

interface AnimalRepo {

    suspend fun getAnimalById(animalId: Int): Animal?

    fun getPagingAnimalsList(categoryType: CategoryType, itemId: Int?, pageSize: Int): Flow<PagingData<AnimalData>>

}