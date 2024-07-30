package com.masterplus.animals.features.bio_detail.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.masterplus.animals.core.domain.repo.AnimalRepo
import com.masterplus.animals.features.bio_detail.presentation.mapper.toFeatureSection2
import com.masterplus.animals.features.bio_detail.presentation.mapper.toFeatureSection3
import com.masterplus.animals.features.bio_detail.presentation.mapper.toScientificNomenclatureSection
import com.masterplus.animals.features.bio_detail.presentation.mapper.toTitleSections
import com.masterplus.animals.features.bio_detail.presentation.navigation.BioDetailRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BioDetailViewModel(
    private val animalRepo: AnimalRepo,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val args = savedStateHandle.toRoute<BioDetailRoute>()

    private val _state = MutableStateFlow(BioDetailState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val animal = animalRepo.getAnimalById(args.id)
            _state.update { it.copy(
                animal = animal,
                isLoading = false
            ) }
        }

        _state
            .map { it.animal }
            .filterNotNull()
            .distinctUntilChanged()
            .onEach { animal ->
                _state.update { it.copy(
                    titleSectionModels = animal.toTitleSections(),
                    scientificNomenclatureSection = animal.toScientificNomenclatureSection(),
                    featureSection2 = animal.toFeatureSection2(),
                    featureSection3 = animal.toFeatureSection3()
                ) }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: BioDetailAction){
        when(action){
            is BioDetailAction.ChangePage -> {
                _state.update { it.copy(
                    selectedPage = action.page
                ) }
            }
        }
    }
}