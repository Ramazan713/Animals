package com.masterplus.animals.test_utils.models

import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.ClassModel
import com.masterplus.animals.core.domain.models.ImageWithMetadata
import com.masterplus.animals.core.presentation.utils.SampleDatas

fun classModel(
    id: Int = 1,
    scientificName: String = "Scientific Name $id",
    className: String = "Class Name $id",
    phylumId: Int = 1,
    kingdomType: KingdomType = KingdomType.DEFAULT,
    image: ImageWithMetadata? = SampleDatas.imageWithMetadata
): ClassModel {
    return ClassModel(
        id, scientificName, className, phylumId, kingdomType, image
    )
}
