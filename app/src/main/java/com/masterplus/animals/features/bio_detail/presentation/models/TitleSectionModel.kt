package com.masterplus.animals.features.bio_detail.presentation.models

data class TitleSectionModel(
    val sectionTitle: String?,
    val titleContents: List<TitleContentModel>,
    val imageUrl: String? = null
)
