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
import com.masterplus.animals.features.category_list.presentation.navigation.CategoryListRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class CategoryListViewModel(
    savedStateHandle: SavedStateHandle,
    private val categoryRepo: CategoryRepo
): ViewModel() {

    val args = savedStateHandle.toRoute<CategoryListRoute>()

    private val _state = MutableStateFlow(CategoryState())
    val state = _state.asStateFlow()

    val pagingItems = args.categoryType.let { categoryType ->
        when(categoryType){
            CategoryType.Class -> {
                categoryRepo.getPagingClasses(10)
                    .map { items -> items.map { it.toImageWithTitleModel() } }
            }
            CategoryType.Order -> {
                categoryRepo.getPagingOrders(10)
                    .map { items -> items.map { it.toImageWithTitleModel() } }
            }
            CategoryType.Family -> {
                categoryRepo.getPagingFamilies(10)
                    .map { items -> items.map { it.toImageWithTitleModel() } }
            }
            else -> flowOf()
        }
    }.cachedIn(viewModelScope)


    init {
        _state.update { it.copy(
            title = args.categoryType.title,
            collectionName = args.categoryType.title,
        ) }
    }

    fun onAction(action: CategoryAction){

    }
}