package com.masterplus.animals.features.species_detail.presentation.mapper

import com.masterplus.animals.core.domain.models.SpeciesDetail
import com.masterplus.animals.features.species_detail.presentation.models.TitleContentModel


fun SpeciesDetail.toScientificNomenclatureSection(): List<TitleContentModel>{
    val titleContents = mutableListOf<TitleContentModel>()

    titleContents.add(
        TitleContentModel(
            title = "Şube",
            content = getScientificNameWithName(phylum.scientificName, phylum.phylum)
        )
    )

    titleContents.add(
        TitleContentModel(
            title = "Sınıf",
            content = getScientificNameWithName(classModel.scientificName, classModel.className)
        )
    )

    titleContents.add(
        TitleContentModel(
            title = "Takım",
            content = getScientificNameWithName(order.scientificName, order.order)
        )
    )

    titleContents.add(
        TitleContentModel(
            title = "Familya",
            content = getScientificNameWithName(family.scientificName, family.family)
        )
    )

    titleContents.add(
        TitleContentModel(
            title = "Cins",
            content = getScientificNameWithName(genus.scientificName, genus.genus)
        )
    )

    titleContents.add(
        TitleContentModel(
            title = "Tür",
            content = getScientificNameWithName(species.scientificName, species.name)
        )
    )

    return titleContents.mapNotNull { titleContent ->
        if(titleContent.content.isNullOrBlank()) return@mapNotNull null
        titleContent
    }
}

private fun getScientificNameWithName(
    scientificName: String,
    name: String?
): String{
    if(name.isNullOrBlank()) return scientificName
    return "$scientificName (${name})"
}
