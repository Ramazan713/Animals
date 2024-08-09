package com.masterplus.animals.features.species_detail.presentation.models

data class TitleSectionModel(
    val sectionTitle: String?,
    val titleContents: List<TitleContentModel>,
    val imageUrl: String? = null
)
