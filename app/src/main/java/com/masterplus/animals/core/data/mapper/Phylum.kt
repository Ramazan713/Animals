package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.models.PhylumModel
import com.masterplus.animals.core.shared_features.database.entity.PhylumEntity


fun PhylumEntity.toPhylumModel(): PhylumModel{
    return PhylumModel(
        id = id,
        scientificName = scientific_name,
        phylum = phylum_tr,
        kingdomId = kingdom_id
    )
}