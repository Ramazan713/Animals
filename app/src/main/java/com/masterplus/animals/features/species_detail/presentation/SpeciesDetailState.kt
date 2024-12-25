package com.masterplus.animals.features.species_detail.presentation

import com.masterplus.animals.core.domain.models.Animal
import com.masterplus.animals.core.domain.models.ISpeciesType
import com.masterplus.animals.core.domain.models.ImageWithMetadata
import com.masterplus.animals.core.domain.models.Plant
import com.masterplus.animals.core.domain.models.SpeciesDetail
import com.masterplus.animals.core.domain.models.SpeciesImageModel
import com.masterplus.animals.core.domain.models.SpeciesModel
import com.masterplus.animals.core.domain.utils.UiText
import com.masterplus.animals.features.species_detail.domain.enums.SpeciesInfoPageEnum
import com.masterplus.animals.features.species_detail.presentation.models.TitleContentModel
import com.masterplus.animals.features.species_detail.presentation.models.TitleSectionModel

data class SpeciesDetailState(
    val isLoading: Boolean = false,
    val selectedPage: SpeciesInfoPageEnum = SpeciesInfoPageEnum.Info,
    val isFavorited: Boolean = false,
    val isListSelected: Boolean = false,
    val listIdControl: Int? = null,
    val species: SpeciesModel? = null,
    val images: List<SpeciesImageModel> = emptyList(),
    val titleSectionModels: List<TitleSectionModel> = emptyList(),
    val scientificNomenclatureSection: List<TitleContentModel> = emptyList(),
    val featureSection2: List<TitleContentModel> = emptyList(),
    val featureSection3: List<TitleContentModel> = emptyList(),
    val dialogEvent: SpeciesDetailDialogEvent? = null,
    val message: UiText? = null
)
