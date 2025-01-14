package com.masterplus.animals.features.category_list.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
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
import com.masterplus.animals.features.category_list.presentation.navigation.CategoryListWithDetailRoute
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class CategoryListWithDetailViewModel(
    private val categoryRepo: CategoryRepo,
    savedStateHandle: SavedStateHandle,
    translationRepo: TranslationRepo,
    appConfigPreferences: AppConfigPreferences
): BaseCategoryListViewModel(
    appConfigPreferences = appConfigPreferences,
    translationRepo = translationRepo,
    kingdomType = savedStateHandle.toRoute<CategoryListWithDetailRoute>().kingdomType,
    categoryType = savedStateHandle.toRoute<CategoryListWithDetailRoute>().categoryType,
    categoryItemId = savedStateHandle.toRoute<CategoryListWithDetailRoute>().categoryItemId,
) {

    val args = savedStateHandle.toRoute<CategoryListWithDetailRoute>()

    init {
        translationRepo
            .getFlowLanguage()
            .onEach { language ->
                _state.update { it.copy(
                    isLoading = true,
                    showAllImageInHeader = false
                ) }
                when(args.categoryType){
                    CategoryType.Class -> {
                        val classModel = categoryRepo.getClassWithId(args.categoryItemId, language) ?: return@onEach
                        _state.update { it.copy(
                            title = classModel.className,
                            subTitle = classModel.scientificName,
                            parentImageData = classModel.image,
                            collectionName = "Takımlar",
                            isLoading = false,
                        ) }
                    }
                    CategoryType.Order -> {
                        val orderModel = categoryRepo.getOrderWithId(args.categoryItemId, language)?: return@onEach
                        _state.update { it.copy(
                            title = orderModel.order,
                            subTitle = orderModel.scientificName,
                            parentImageData = orderModel.image,
                            collectionName = "Familyalar",
                            isLoading = false,
                        ) }
                    }
                    else -> Unit
                }
            }
            .launchIn(viewModelScope)
    }

    override fun getPagingFlow(language: LanguageEnum, targetItemId: Int?, pageSize: Int): Flow<PagingData<CategoryData>>{
        return when (args.categoryType) {
            CategoryType.Class -> {
                categoryRepo.getPagingOrdersWithClassId(args.categoryItemId, pageSize, language, args.kingdomType, targetItemId = targetItemId)
                    .map { items -> items.map { it.toCategoryData() } }
            }
            CategoryType.Order -> {
                categoryRepo.getPagingFamiliesWithOrderId(args.categoryItemId, pageSize, language, args.kingdomType, targetItemId = targetItemId)
                    .map { items -> items.map { it.toCategoryData() } }
            }
            else -> flowOf()
        }
    }
}