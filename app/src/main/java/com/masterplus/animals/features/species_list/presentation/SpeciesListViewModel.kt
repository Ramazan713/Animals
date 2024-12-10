package com.masterplus.animals.features.species_list.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.cachedIn
import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.repo.CategoryRepo
import com.masterplus.animals.core.domain.repo.SpeciesRepo
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointSaveMode
import com.masterplus.animals.core.shared_features.savepoint.domain.repo.SavePointRepo
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import com.masterplus.animals.core.shared_features.translation.domain.repo.TranslationRepo
import com.masterplus.animals.features.species_list.presentation.navigation.SpeciesListRoute
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class SpeciesListViewModel(
    private val speciesRepo: SpeciesRepo,
    private val categoryRepo: CategoryRepo,
    private val savePointRepo: SavePointRepo,
    private val translationRepo: TranslationRepo,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val args = savedStateHandle.toRoute<SpeciesListRoute>()
    
    private val _state = MutableStateFlow(SpeciesListState())
    val state = _state.asStateFlow()

    val pagingItems = translationRepo
        .getFlowLanguage()
        .flatMapLatest { language ->
            args.let { args ->
                speciesRepo.getPagingSpeciesList(args.categoryType, args.realItemId, 20, language, args.kingdomType)
            }
        }
        .cachedIn(viewModelScope)

    init {
        translationRepo
            .getFlowLanguage()
            .onEach { language ->
                val itemId = args.realItemId
                setTitle(itemId, language)
            }
            .launchIn(viewModelScope)

        _state.update { it.copy(
            listIdControl = if(args.categoryType == CategoryType.List) args.realItemId else null
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
                    savePointRepo.insertContentSavePoint(
                        title = "Sample",
                        destination = SavePointDestination.fromCategoryType(
                            categoryType = args.categoryType,
                            destinationId = args.realItemId,
                            kingdomType = args.kingdomType
                        ),
                        itemPosIndex = action.posIndex,
                        saveMode = SavePointSaveMode.Manuel
                    )
                }
            }
        }
    }


    private suspend fun setTitle(itemId: Int?, language: LanguageEnum){
        val kingdomTitle = if(args.kingdomType?.isPlants == true) "Bitkiler Listesi" else "Hayvanlar Listesi"
        var title: UiText = UiText.Text(kingdomTitle)

        if(itemId != null){
            title = categoryRepo.getCategoryName(args.categoryType, itemId, language) ?: title
        }
        _state.update { it.copy(
            title = title
        ) }
    }
}