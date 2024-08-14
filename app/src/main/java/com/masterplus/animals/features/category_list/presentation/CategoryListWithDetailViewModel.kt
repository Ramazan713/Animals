package com.masterplus.animals.features.category_list.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.cachedIn
import androidx.paging.map
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.repo.CategoryRepo
import com.masterplus.animals.core.presentation.mapper.toImageWithTitleModel
import com.masterplus.animals.features.category_list.presentation.navigation.CategoryListWithDetailRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CategoryListWithDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val categoryRepo: CategoryRepo
): ViewModel() {

    val args = savedStateHandle.toRoute<CategoryListWithDetailRoute>()

    private val _state = MutableStateFlow(CategoryState())
    val state = _state.asStateFlow()

    val pagingItems = args.categoryType.let { categoryType ->
        when (categoryType) {
            CategoryType.Class -> {
                categoryRepo.getPagingOrdersWithClassId(args.itemId,10)
                    .map { items ->
                        items.map { it.toImageWithTitleModel() }
                    }
            }
            CategoryType.Order -> {
                categoryRepo.getPagingFamiliesWithOrderId(args.itemId,10)
                    .map { items ->
                        items.map { it.toImageWithTitleModel() }
                    }
            }
            else -> flowOf()
        }
    }.cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            _state.update { it.copy(
                isLoading = true
            ) }
            when(args.categoryType){
                CategoryType.Class -> {
                    val classModel = categoryRepo.getClassWithId(args.itemId) ?: return@launch
                    _state.update { it.copy(
                        title = classModel.scientificName,
                        subTitle = classModel.className,
                        parentImageData = classModel.imageUrl,
                        collectionName = "Takımlar",
                        isLoading = false
                    ) }
                }
                CategoryType.Order -> {
                    val orderModel = categoryRepo.getOrderWithId(args.itemId)?: return@launch
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
    }

    fun onAction(action: CategoryAction){

    }
}