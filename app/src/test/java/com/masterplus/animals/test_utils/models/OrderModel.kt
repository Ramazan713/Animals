package com.masterplus.animals.test_utils.models

import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.ImageWithMetadata
import com.masterplus.animals.core.domain.models.OrderModel
import com.masterplus.animals.core.presentation.utils.SampleDatas

fun orderModel(
    id: Int = 1,
    scientificName: String = "Scientific Name $id",
    order: String = "Order $id",
    classId: Int = 1,
    kingdomType: KingdomType = KingdomType.DEFAULT,
    image: ImageWithMetadata? = SampleDatas.imageWithMetadata,
): OrderModel {
    return OrderModel(
        id, scientificName, order, classId, kingdomType, image
    )
}
