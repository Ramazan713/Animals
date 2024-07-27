package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.models.SpeciesModel
import com.masterplus.animals.core.shared_features.database.entity.SpeciesEntity

fun SpeciesEntity.toSpecies(): SpeciesModel {
    return SpeciesModel(
        id = id,
        scientificName = scientific_name,
        species = species_tr,
        genusId = genus_id
    )
}
