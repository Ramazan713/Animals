package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.domain.models.OrderModel
import com.masterplus.animals.core.shared_features.database.entity.OrderEntity
import com.masterplus.animals.core.shared_features.database.entity_helper.OrderWithImageEmbedded
import com.masterplus.animals.core.shared_features.translation.domain.enums.LanguageEnum

fun OrderWithImageEmbedded.toOrder(
    language: LanguageEnum
): OrderModel {
    return with(order){
        OrderModel(
            id = id,
            scientificName = scientific_name,
            order = if(language.isEn) order_en else order_tr,
            classId = class_id,
            image = image?.toImageWithMetadata(),
            kingdomType = KingdomType.fromKingdomId(kingdom_id),
            orderKey = order_key
        )
    }
}


fun OrderEntity.toOrder(
    language: LanguageEnum
): OrderModel {
    return OrderModel(
        id = id,
        scientificName = scientific_name,
        order = if(language.isEn) order_en else order_tr,
        classId = class_id,
        image = null,
        kingdomType = KingdomType.fromKingdomId(kingdom_id),
        orderKey = order_key
    )
}


fun OrderModel.toCategoryData(): CategoryData {
    return CategoryData(
        id = id,
        image = image,
        title = scientificName,
        secondaryTitle = order,
        categoryType = CategoryType.Order,
        kingdomType = kingdomType,
        orderKey = orderKey
    )
}