package com.masterplus.animals.features.species_detail.presentation.models

import com.masterplus.animals.core.domain.models.ImageWithMetadata

data class TitleSectionModel(
    val sectionTitle: String?,
    val titleContents: List<TitleContentModel>,
    val image: ImageWithMetadata? = null
)
