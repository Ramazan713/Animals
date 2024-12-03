package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.models.AnimalDetail
import com.masterplus.animals.core.shared_features.database.entity_helper.AnimalDetailEmbedded
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum


fun AnimalDetailEmbedded.toAnimalDetail(
    language: LanguageEnum
): AnimalDetail{
    return AnimalDetail(
        animal = animal.toAnimal(
            species = species,
            images = images,
            language = language
        ),
        phylum = phylum.toPhylumModel(language),
        classModel = classEntity.toClass(language),
        order = order.toOrder(language),
        family = family.toFamily(language),
        genus = genus.toGenus(language),
        species = species.toSpecies(language),
        habitatCategories = habitatCategories.map { it.toHabitatCategory(language) },
        images = images.map { it.toSpeciesImage() }
    )
}