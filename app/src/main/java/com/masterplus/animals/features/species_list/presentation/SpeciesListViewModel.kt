package com.masterplus.animals.features.species_list.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.masterplus.animals.core.data.utils.RemoteKeyUtil
import com.masterplus.animals.core.domain.constants.KPref
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.models.SpeciesListDetail
import com.masterplus.animals.core.domain.repo.CategoryRepo
import com.masterplus.animals.core.domain.repo.SpeciesRepo
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.shared_features.preferences.data.get
import com.masterplus.animals.core.shared_features.preferences.domain.AppPreferences
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointSaveMode
import com.masterplus.animals.core.shared_features.savepoint.domain.repo.SavePointRepo
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import com.masterplus.animals.core.shared_features.translation.domain.repo.TranslationRepo
import com.masterplus.animals.features.species_list.presentation.navigation.SpeciesListRoute
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class SpeciesListViewModel(
    private val speciesRepo: SpeciesRepo,
    private val categoryRepo: CategoryRepo,
    private val savePointRepo: SavePointRepo,
    private val appPreferences: AppPreferences,
    translationRepo: TranslationRepo,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val args = savedStateHandle.toRoute<SpeciesListRoute>()

    private val _state = MutableStateFlow(SpeciesListState())
    val state = _state.asStateFlow()

    private val _targetItemIdFlow = MutableStateFlow<Int?>(null)
    private val speciesPageSizeFlow = appPreferences.dataFlow.map { it[KPref.speciesPageSize] }.distinctUntilChanged()


    val pagingItems = combine(
        translationRepo
            .getFlowLanguage(),
        _targetItemIdFlow,
        speciesPageSizeFlow
    ){ language, targetItemId, speciesPageSize ->
        getPagingFlow(language, targetItemId, speciesPageSize)
    }.flatMapLatest { flow -> flow  }
        .cachedIn(viewModelScope)

    init {
        translationRepo
            .getFlowLanguage()
            .onEach { language ->
                val itemId = args.categoryItemId
                setTitle(itemId, language)
            }
            .launchIn(viewModelScope)

        _state.update { it.copy(
            listIdControl = args.categoryType.toListIdControlOrNull(args.categoryItemId),
        ) }
    }

    fun onAction(action: SpeciesListAction){
        when(action){
            is SpeciesListAction.ShowDialog -> {
                _state.update { it.copy(
                    dialogEvent = action.dialogEvent
                ) }
            }
            is SpeciesListAction.SavePosition -> {
                viewModelScope.launch {
                    savePointRepo.insertSavePoint(
                        title = "Sample",
                        destination = SavePointDestination.fromCategoryType(
                            categoryType = args.categoryType,
                            destinationId = args.categoryItemId,
                            kingdomType = args.kingdomType
                        ),
                        itemId = action.posIndex,
                        saveMode = SavePointSaveMode.Manuel,
                        contentType = SavePointContentType.Content
                    )
                }
            }
            is SpeciesListAction.SetPagingTargetId -> {
                _targetItemIdFlow.value = action.targetId
            }
        }
    }

    private fun getPagingFlow(language: LanguageEnum, targetItemId: Int?, pageSize: Int): Flow<PagingData<SpeciesListDetail>>{
        return with(args){
            when {
                categoryItemId == null -> speciesRepo.getPagingSpeciesWithKingdom(
                    pageSize = pageSize,
                    kingdom = kingdomType,
                    targetItemId = targetItemId,
                    language = language
                )
                categoryType == CategoryType.List -> speciesRepo.getPagingSpeciesWithList(
                    pageSize = pageSize,
                    targetItemId = targetItemId,
                    language = language,
                    itemId = categoryItemId
                )
                else -> speciesRepo.getPagingSpeciesWithCategory(
                    pageSize = pageSize,
                    kingdom = kingdomType,
                    targetItemId = targetItemId,
                    language = language,
                    categoryType = categoryType,
                    itemId = categoryItemId
                )

            }
        }
    }

    private suspend fun setTitle(itemId: Int?, language: LanguageEnum){
        val kingdomTitle = if(args.kingdomType.isPlants) "Bitkiler Listesi" else "Hayvanlar Listesi"
        var title: UiText = UiText.Text(kingdomTitle)

        if(itemId != null){
            title = categoryRepo.getCategoryName(args.categoryType, itemId, language)
                ?.let { UiText.Text(it) }
                ?: title
        }
        _state.update { it.copy(
            title = title
        ) }
    }
}