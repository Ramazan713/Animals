package com.masterplus.animals.features.species_detail.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.ImageWithMetadata
import com.masterplus.animals.core.domain.models.SpeciesImageModel
import com.masterplus.animals.core.domain.repo.AnimalRepo
import com.masterplus.animals.core.domain.repo.PlantRepo
import com.masterplus.animals.core.domain.repo.SpeciesRepo
import com.masterplus.animals.core.shared_features.list.domain.repo.ListSpeciesRepo
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum
import com.masterplus.animals.core.shared_features.translation.domain.repo.TranslationRepo
import com.masterplus.animals.features.species_detail.presentation.mapper.toFeatureSection2
import com.masterplus.animals.features.species_detail.presentation.mapper.toFeatureSection3
import com.masterplus.animals.features.species_detail.presentation.mapper.toScientificNomenclatureSection
import com.masterplus.animals.features.species_detail.presentation.mapper.toTitleSections
import com.masterplus.animals.features.species_detail.presentation.navigation.SpeciesDetailRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class SpeciesDetailViewModel(
    private val animalRepo: AnimalRepo,
    private val speciesRepo: SpeciesRepo,
    private val plantRepo: PlantRepo,
    private val translationRepo: TranslationRepo,
    private val listSpeciesRepo: ListSpeciesRepo,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val args = savedStateHandle.toRoute<SpeciesDetailRoute>()

    private val _state = MutableStateFlow(SpeciesDetailState())
    val state = _state.asStateFlow()

    init {
        initListening()
    }

    fun onAction(action: SpeciesDetailAction){
        when(action){
            is SpeciesDetailAction.ChangePage -> {
                _state.update { it.copy(
                    selectedPage = action.page
                ) }
            }

            is SpeciesDetailAction.ShowDialog -> {
                _state.update { it.copy(
                    dialogEvent = action.dialogEvent
                ) }
            }

            SpeciesDetailAction.ClearMessage -> _state.update { it.copy(message = null) }
        }
    }

    private fun getTitleSectionImages(images: List<SpeciesImageModel>): List<ImageWithMetadata>{
        return images.map { it.image }
    }

    private fun initListening(){

        _state.update { it.copy(
            listIdControl = args.listIdControl
        ) }

        translationRepo
            .getFlowLanguage()
            .onEach { language ->
                loadSpeciesData(language)
            }
            .launchIn(viewModelScope)

        listSpeciesRepo.getHasSpeciesInListFlow(
            speciesId = args.speciesId,
            isListFavorite = false
        ).onEach { isListSelected ->
            _state.update { it.copy(isListSelected = isListSelected) }
        }.launchIn(viewModelScope)

        listSpeciesRepo.getHasSpeciesInListFlow(
            speciesId = args.speciesId,
            isListFavorite = true
        ).onEach { isFavorited ->
            _state.update { it.copy(isFavorited = isFavorited) }
        }.launchIn(viewModelScope)

    }

    private suspend fun loadSpeciesData(language: LanguageEnum){
        _state.update { it.copy(isLoading = true) }
        val species = speciesRepo.getSpeciesById(args.speciesId, language)
        if(species != null){
            val checkResult = speciesRepo.checkSpeciesDetailData(species)
            if (checkResult.isError){
                _state.update { it.copy(
                    isLoading = false,
                    message = checkResult.getFailureError?.text,
                    species = species
                ) }
                return
            }
            when(species.kingdomType){
                KingdomType.Animals -> {
                    animalRepo.getAnimalDetailBySpeciesId(args.speciesId, language)?.let { animalDetail ->
                        val titleSectionImages = getTitleSectionImages(animalDetail.images)
                        _state.update { it.copy(
                            images = animalDetail.images,
                            titleSectionModels = animalDetail.detail.toTitleSections(images = titleSectionImages),
                            scientificNomenclatureSection = animalDetail.toScientificNomenclatureSection(),
                            featureSection2 = animalDetail.toFeatureSection2(),
                            featureSection3 = animalDetail.detail.toFeatureSection3()
                        ) }
                    }
                }
                KingdomType.Plants -> {
                    plantRepo.getPlantDetailBySpeciesId(args.speciesId, language)?.let { plantDetail ->
                        val titleSectionImages = getTitleSectionImages(plantDetail.images)
                        _state.update { it.copy(
                            images = plantDetail.images,
                            titleSectionModels = plantDetail.detail.toTitleSections(images = titleSectionImages),
                            scientificNomenclatureSection = plantDetail.toScientificNomenclatureSection(),
                            featureSection2 = plantDetail.toFeatureSection2(),
                            featureSection3 = plantDetail.detail.toFeatureSection3()
                        ) }
                    }
                }
            }
        }
        _state.update { it.copy(
            isLoading = false,
            species = species
        ) }
    }

}