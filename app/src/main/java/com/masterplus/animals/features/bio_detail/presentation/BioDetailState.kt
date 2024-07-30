package com.masterplus.animals.features.bio_detail.presentation

import com.masterplus.animals.core.domain.models.Animal
import com.masterplus.animals.features.bio_detail.domain.enums.BioInfoPageEnum
import com.masterplus.animals.features.bio_detail.presentation.mapper.toFeatureSection2
import com.masterplus.animals.features.bio_detail.presentation.mapper.toFeatureSection3
import com.masterplus.animals.features.bio_detail.presentation.mapper.toScientificNomenclatureSection
import com.masterplus.animals.features.bio_detail.presentation.mapper.toTitleSections
import com.masterplus.animals.features.bio_detail.presentation.models.TitleContentModel
import com.masterplus.animals.features.bio_detail.presentation.models.TitleSectionModel

data class BioDetailState(
    val isLoading: Boolean = false,
    val selectedPage: BioInfoPageEnum = BioInfoPageEnum.Info,
    val animal: Animal? = null,
    val titleSectionModels: List<TitleSectionModel> = emptyList(),
    val scientificNomenclatureSection: List<TitleContentModel> = emptyList(),
    val featureSection2: List<TitleContentModel> = emptyList(),
    val featureSection3: List<TitleContentModel> = emptyList()
)
