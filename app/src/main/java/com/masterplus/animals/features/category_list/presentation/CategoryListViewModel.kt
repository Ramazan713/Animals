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
import com.masterplus.animals.core.shared_features.translation.domain.repo.TranslationRepo
import com.masterplus.animals.features.category_list.presentation.navigation.CategoryListRoute
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class CategoryListViewModel(
    savedStateHandle: SavedStateHandle,
    private val categoryRepo: CategoryRepo,
    private val translationRepo: TranslationRepo
): ViewModel() {

    val args = savedStateHandle.toRoute<CategoryListRoute>()

    private val _state = MutableStateFlow(CategoryState(
        kingdomType = args.kingdomType
    ))
    val state = _state.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingItems = translationRepo
        .getFlowLanguage()
        .flatMapLatest { language ->
            args.categoryType.let { categoryType ->
                when(categoryType){
                    CategoryType.Class -> {
                        categoryRepo.getPagingClasses(10, language, args.kingdomType)
                            .map { items -> items.map { it.toImageWithTitleModel() } }
                    }
                    CategoryType.Order -> {
                        categoryRepo.getPagingOrders(10, language, args.kingdomType)
                            .map { items -> items.map { it.toImageWithTitleModel() } }
                    }
                    CategoryType.Family -> {
                        categoryRepo.getPagingFamilies(10, language, args.kingdomType)
                            .map { items -> items.map { it.toImageWithTitleModel() } }
                    }
                    else -> flowOf()
                }
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