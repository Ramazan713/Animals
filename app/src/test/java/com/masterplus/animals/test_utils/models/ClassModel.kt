package com.masterplus.animals.test_utils.models

import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.ClassModel

fun classModel(
    id: Int = 1,
    scientificName: String = "Scientific Name $id",
    className: String = "Class Name $id",
    phylumId: Int = 1,
    kingdomType: KingdomType = KingdomType.DEFAULT,
    imagePath: String? = "imagePathClass",
    imageUrl: String? = "https://example.com/class_image.jpg"
): ClassModel {
    return ClassModel(
        id, scientificName, className, phylumId, kingdomType, imagePath, imageUrl
    )
}
