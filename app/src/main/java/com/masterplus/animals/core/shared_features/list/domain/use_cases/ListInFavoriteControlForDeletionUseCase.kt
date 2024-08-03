package com.masterplus.animals.core.shared_features.list.domain.use_cases

import com.masterplus.animals.core.shared_features.list.domain.repo.ListRepo
import javax.inject.Inject

class ListInFavoriteControlForDeletionUseCase @Inject constructor(
    private val listRepo: ListRepo
) {
    suspend operator fun invoke(listId: Int?, isInFavorite: Boolean?): Boolean{
        if(isInFavorite == false) return false
        if(listId == null) return false
        return listRepo.isFavoriteList(listId)
    }
}