package com.masterplus.animals.test_utils.models

import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.FamilyModel
import com.masterplus.animals.core.domain.models.ImageWithMetadata
import com.masterplus.animals.core.presentation.utils.SampleDatas

fun familyModel(
    id: Int = 1,
    orderKey: Int = 1,
    scientificName: String = "Scientific Name $id",
    family: String = "Family $id",
    orderId: Int = 1,
    kingdomType: KingdomType = KingdomType.DEFAULT,
    image: ImageWithMetadata? = SampleDatas.imageWithMetadata,
): FamilyModel {
    return FamilyModel(
        id, orderKey, scientificName, family, orderId, kingdomType, image
    )
}
