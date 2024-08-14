package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.domain.models.OrderModel
import com.masterplus.animals.core.shared_features.database.entity.OrderEntity

fun OrderEntity.toOrder(): OrderModel {
    return OrderModel(
        id = id,
        scientificName = scientific_name,
        order = order_tr,
        classId = class_id,
        imagePath = image_path,
        imageUrl = image_url
    )
}


fun OrderModel.toCategoryData(): CategoryData {
    return CategoryData(
        id = id,
        imageUrl = imageUrl,
        title = scientificName,
        secondaryTitle = order,
        categoryType = CategoryType.Order
    )
}