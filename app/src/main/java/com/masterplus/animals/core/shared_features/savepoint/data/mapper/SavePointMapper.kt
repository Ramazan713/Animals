package com.masterplus.animals.core.shared_features.savepoint.data.mapper

import com.masterplus.animals.core.shared_features.database.entity.SavePointEntity
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import com.masterplus.animals.core.shared_features.savepoint.domain.models.SavePoint
import kotlinx.datetime.LocalDateTime


fun SavePointEntity.toSavePoint(): SavePoint?{
    return SavePoint(
        id = id,
        title = titleTr,
        contentType = SavePointContentType.from(contentTypeId) ?: return null,
        destination = SavePointDestination.from(
            destinationId = destinationId,
            destinationTypeId = destinationTypeId,
            saveKey = saveKey
        ) ?: return null,
        itemPosIndex = itemPosIndex,
        modifiedTime = LocalDateTime.parse(modifiedTime),
        imageData = imageUrl,
        imagePath = imagePath
    );
}

fun SavePoint.toSavePointEntity(): SavePointEntity{
    return SavePointEntity(
        id = id,
        titleTr = title,
        titleEn = title,
        contentTypeId = contentType.contentTypeId,
        destinationId = destination.destinationId,
        destinationTypeId = destination.destinationTypeId,
        saveKey = destination.saveKey,
        itemPosIndex = itemPosIndex,
        modifiedTime = modifiedTime.toString(),
        imageUrl = imageData?.toString(),
        imagePath = imagePath
    );
}
