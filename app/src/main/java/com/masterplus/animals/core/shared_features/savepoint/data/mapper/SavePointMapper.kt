package com.masterplus.animals.core.shared_features.savepoint.data.mapper

import com.masterplus.animals.core.domain.enums.KingdomType
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
            saveKey = saveKey,
            kingdomType = KingdomType.fromKingdomId(kingdomId)
        ) ?: return null,
        itemPosIndex = itemPosIndex,
        modifiedTime = LocalDateTime.parse(modifiedTime),
        imageData = imageUrl,
        imagePath = imagePath,
        kingdomType = KingdomType.fromKingdomId(kingdomId)
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
        imagePath = imagePath,
        kingdomId = kingdomType.kingdomId
    );
}
