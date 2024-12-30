package com.masterplus.animals.features.category_list.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.masterplus.animals.core.data.mapper.toCategoryData
import com.masterplus.animals.core.data.utils.RemoteKeyUtil
import com.masterplus.animals.core.domain.constants.KPref
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.domain.repo.CategoryRepo
import com.masterplus.animals.core.shared_features.preferences.data.get
import com.masterplus.animals.core.shared_features.preferences.domain.AppPreferences
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import com.masterplus.animals.core.shared_features.translation.domain.repo.TranslationRepo
import com.masterplus.animals.features.category_list.presentation.navigation.CategoryListRoute
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class CategoryListViewModel(
    savedStateHandle: SavedStateHandle,
    private val categoryRepo: CategoryRepo,
    private val translationRepo: TranslationRepo,
    private val appPreferences: AppPreferences
): ViewModel() {

    val args = savedStateHandle.toRoute<CategoryListRoute>()

    private val _targetItemIdFlow = MutableStateFlow<Int?>(null)
    private val categoryPageSizeFlow = appPreferences.dataFlow.map { it[KPref.categoryPageSize] }.distinctUntilChanged()

    private val _state = MutableStateFlow(CategoryState(
        kingdomType = args.kingdomType,
        categoryType = args.categoryType,
        categoryItemId = null
    ))
    val state = _state.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingItems = combine(
        translationRepo
            .getFlowLanguage(),
        _targetItemIdFlow,
        categoryPageSizeFlow
    ){ language, targetItemId,categoryPageSize ->
        getPagingFlow(language, targetItemId, categoryPageSize)
    }.flatMapLatest { flow -> flow }
        .cachedIn(viewModelScope)



    init {
        _state.update { it.copy(
            title = args.categoryType.title,
            collectionName = args.categoryType.title,
            showAllImageInHeader = true,
            label = RemoteKeyUtil.getRemoteKeyWithCategoryType(
                categoryType = args.categoryType,
                kingdomType = args.kingdomType,
                parentItemId = null
            )
        ) }
    }

    fun onAction(action: CategoryAction){
        when(action){
            is CategoryAction.ShowDialog -> {
                _state.update { it.copy(
                    dialogEvent = action.dialogEvent
                ) }
            }

            is CategoryAction.SetPagingTargetId -> {
                _targetItemIdFlow.value = action.targetId
            }
        }
    }

    private fun getPagingFlow(language: LanguageEnum, targetItemId: Int?, pageSize: Int): Flow<PagingData<CategoryData>>{
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