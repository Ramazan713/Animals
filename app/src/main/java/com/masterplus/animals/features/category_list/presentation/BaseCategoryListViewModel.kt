package com.masterplus.animals.features.category_list.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.shared_features.preferences.domain.AppConfigPreferences
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import com.masterplus.animals.core.shared_features.translation.domain.repo.TranslationRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

abstract class BaseCategoryListViewModel(
    kingdomType: KingdomType,
    categoryType: CategoryType,
    categoryItemId: Int?,
    appConfigPreferences: AppConfigPreferences,
    translationRepo: TranslationRepo,
): ViewModel() {
    private val _targetItemIdFlow = MutableStateFlow<Int?>(null)

    private val categoryPageSizeFlow = appConfigPreferences.dataFlow
        .map { it.pagination.categoryPageSize }.distinctUntilChanged()

    protected val _state = MutableStateFlow(CategoryState(
        kingdomType = kingdomType,
        categoryType = categoryType,
        categoryItemId = categoryItemId
    ))
    val state = _state.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val pagingItems = combine(
        translationRepo
            .getFlowLanguage(),
        _targetItemIdFlow,
        categoryPageSizeFlow
    ){ language, targetItemId,categoryPageSize ->
        Triple(language, targetItemId, categoryPageSize)
    }.distinctUntilChanged()
        .flatMapLatest { triple ->
            getPagingFlow(triple.first, triple.second, triple.third)
        }.cachedIn(viewModelScope)

    protected abstract fun getPagingFlow(
        language: LanguageEnum,
        targetItemId: Int?,
        pageSize: Int
    ): Flow<PagingData<CategoryData>>

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
}