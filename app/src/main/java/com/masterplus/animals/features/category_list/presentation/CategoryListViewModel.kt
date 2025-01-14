package com.masterplus.animals.features.category_list.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.paging.map
import com.masterplus.animals.core.data.mapper.toCategoryData
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.domain.repo.CategoryRepo
import com.masterplus.animals.core.shared_features.preferences.domain.AppConfigPreferences
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import com.masterplus.animals.core.shared_features.translation.domain.repo.TranslationRepo
import com.masterplus.animals.features.category_list.presentation.navigation.CategoryListRoute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class CategoryListViewModel(
    savedStateHandle: SavedStateHandle,
    private val categoryRepo: CategoryRepo,
    translationRepo: TranslationRepo,
    appConfigPreferences: AppConfigPreferences
): BaseCategoryListViewModel(
    appConfigPreferences = appConfigPreferences,
    translationRepo = translationRepo,
    kingdomType = savedStateHandle.toRoute<CategoryListRoute>().kingdomType,
    categoryType = savedStateHandle.toRoute<CategoryListRoute>().categoryType,
    categoryItemId = null
) {
    val args = savedStateHandle.toRoute<CategoryListRoute>()

    init {
        _state.update { it.copy(
            title = args.categoryType.title,
            collectionName = args.categoryType.title,
            showAllImageInHeader = true,
        ) }
    }

    override fun getPagingFlow(language: LanguageEnum, targetItemId: Int?, pageSize: Int): Flow<PagingData<CategoryData>>{
        return when(args.categoryType){
            CategoryType.Class -> {
                categoryRepo.getPagingClasses(pageSize, language, args.kingdomType, targetItemId = targetItemId)
                    .map { items -> items.map { it.toCategoryData() } }
            }
            CategoryType.Order -> {
                categoryRepo.getPagingOrders(pageSize, language, args.kingdomType, targetItemId = targetItemId)
                    .map { items -> items.map { it.toCategoryData() } }
            }
            CategoryType.Family -> {
                categoryRepo.getPagingFamilies(pageSize, language, args.kingdomType, targetItemId = targetItemId)
                    .map { items -> items.map { it.toCategoryData() } }
            }
            CategoryType.Habitat -> {
                categoryRepo.getPagingHabitats(pageSize, language, args.kingdomType, targetItemId = targetItemId)
                    .map { items -> items.map { it.toCategoryData() } }
            }
            else -> flowOf()
        }
    }
}