package com.masterplus.animals.core.data.mapper

import com.masterplus.animals.core.domain.enums.CategoryType
import com.masterplus.animals.core.domain.models.CategoryData
import com.masterplus.animals.core.domain.models.ClassModel
import com.masterplus.animals.core.shared_features.database.entity.ClassEntity

fun ClassEntity.toClass(): ClassModel {
    return ClassModel(
        id = id,
        scientificName = scientific_name,
        className = class_tr,
        phylumId = phylum_id,
        imagePath = image_path,
        imageUrl = image_url
    )
}


fun ClassModel.toCategoryData(): CategoryData {
    return CategoryData(
        id = id,
        imageUrl = imageUrl,
        title = scientificName,
        secondaryTitle = className,
        categoryType = CategoryType.Class
    )
}