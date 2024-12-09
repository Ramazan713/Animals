package com.masterplus.animals.features.category_list.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.cachedIn
import androidx.paging.map
import com.masterplus.animals.core.data.mapper.toCategoryData
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.repo.CategoryRepo
import com.masterplus.animals.core.presentation.mapper.toImageWithTitleModel
import com.masterplus.animals.core.shared_features.translation.domain.repo.TranslationRepo
import com.masterplus.animals.features.category_list.presentation.navigation.CategoryListWithDetailRoute
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class CategoryListWithDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val categoryRepo: CategoryRepo,
    private val translationRepo: TranslationRepo
): ViewModel() {

    val args = savedStateHandle.toRoute<CategoryListWithDetailRoute>()

    private val _state = MutableStateFlow(CategoryState(
        kingdomType = args.kingdomType,
        categoryType = args.categoryType,
        itemId = args.itemId
    ))
    val state = _state.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingItems = translationRepo
        .getFlowLanguage()
        .flatMapLatest { language ->
            args.categoryType.let { categoryType ->
                when (categoryType) {
                    CategoryType.Class -> {
                        categoryRepo.getPagingOrdersWithClassId(args.itemId, 10, language, args.kingdomType)
                            .map { items ->
                                items.map { it.toCategoryData() }
                            }
                    }

                    CategoryType.Order -> {
                        categoryRepo.getPagingFamiliesWithOrderId(args.itemId, 10, language, args.kingdomType)
                            .map { items ->
                                items.map { it.toCategoryData() }
                            }
                    }

                    else -> flowOf()
                }
            }
    }.cachedIn(viewModelScope)

    init {
        translationRepo
            .getFlowLanguage()
            .onEach { language ->
                _state.update { it.copy(
                    isLoading = true
                ) }
                when(args.categoryType){
                    CategoryType.Class -> {
                        val classModel = categoryRepo.getClassWithId(args.itemId, language) ?: return@onEach
                        _state.update { it.copy(
                            title = classModel.scientificName,
                            subTitle = classModel.className,
                            parentImageData = classModel.imageUrl,
                            collectionName = "Takımlar",
                            isLoading = false
                        ) }
                    }
                    CategoryType.Order -> {
                        val orderModel = categoryRepo.getOrderWithId(args.itemId, language)?: return@onEach
                        _state.update { it.copy(
                            title = orderModel.scientificName,
                            subTitle = orderModel.order,
                            parentImageData = orderModel.imageUrl,
                            collectionName = "Familyalar",
                            isLoading = false
                        ) }
                    }
                    else -> Unit
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: CategoryAction){

    }
}