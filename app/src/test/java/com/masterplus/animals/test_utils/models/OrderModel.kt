package com.masterplus.animals.test_utils.models

import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.OrderModel

fun orderModel(
    id: Int = 1,
    scientificName: String = "Scientific Name $id",
    order: String = "Order $id",
    classId: Int = 1,
    kingdomType: KingdomType = KingdomType.DEFAULT,
    imagePath: String? = "imagePathOrder",
    imageUrl: String? = "https://example.com/order_image.jpg"
): OrderModel {
    return OrderModel(
        id, scientificName, order, classId, kingdomType, imagePath, imageUrl
    )
}
