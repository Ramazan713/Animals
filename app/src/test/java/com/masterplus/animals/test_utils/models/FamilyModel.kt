package com.masterplus.animals.test_utils.models

import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.FamilyModel

fun familyModel(
    id: Int = 1,
    scientificName: String = "Scientific Name $id",
    family: String = "Family $id",
    orderId: Int = 1,
    kingdomType: KingdomType = KingdomType.DEFAULT,
    imagePath: String? = "imagePathFamily",
    imageUrl: String? = "https://example.com/family_image.jpg"
): FamilyModel {
    return FamilyModel(
        id, scientificName, family, orderId, kingdomType, imagePath, imageUrl
    )
}
