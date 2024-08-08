package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.models.AnimalDetail
import com.masterplus.animals.core.shared_features.database.entity_helper.AnimalDetailEmbedded


fun AnimalDetailEmbedded.toAnimalDetail(): AnimalDetail{
    return AnimalDetail(
        animal = animal.toAnimal(
            species = species,
            images = images
        ),
        phylum = phylum.toPhylumModel(),
        classModel = classEntity.toClass(),
        order = order.toOrder(),
        family = family.toFamily(),
        genus = genus.toGenus(),
        species = species.toSpecies(),
        habitatCategory = habitatCategory.toHabitatCategory(),
        images = images.map { it.toAnimalImage() }
    )
}