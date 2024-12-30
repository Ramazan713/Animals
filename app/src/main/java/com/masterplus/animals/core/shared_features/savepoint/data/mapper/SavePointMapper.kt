package com.masterplus.animals.core.shared_features.savepoint.data.mapper

import com.masterplus.animals.core.data.mapper.toImageWithMetadata
import com.masterplus.animals.core.domain.enums.KingdomType
import com.masterplus.animals.core.shared_features.database.entity.SavePointEntity
import com.masterplus.animals.core.shared_features.database.entity_helper.SavePointWithImageEmbedded
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointContentType
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointDestination
import com.masterplus.animals.core.shared_features.savepoint.domain.enums.SavePointSaveMode
import com.masterplus.animals.core.shared_features.savepoint.domain.models.SavePoint
import kotlinx.datetime.LocalDateTime


fun SavePointWithImageEmbedded.toSavePoint(): SavePoint?{
    return with(savepoint){
        SavePoint(
            id = id,
            title = titleTr,
            contentType = SavePointContentType.from(contentTypeId) ?: return null,
            destination = SavePointDestination.from(
                destinationId = destinationId,
                destinationTypeId = destinationTypeId,
                saveKey = saveKey,
                kingdomType = KingdomType.fromKingdomId(kingdomId)
            ) ?: return null,
            itemId = itemId,
            modifiedTime = LocalDateTime.parse(modifiedTime),
            image = image?.toImageWithMetadata(),
            kingdomType = KingdomType.fromKingdomId(kingdomId),
            saveMode = SavePointSaveMode.fromModeId(saveModeId)
        )
    }
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
        itemId = itemId,
        modifiedTime = modifiedTime.toString(),
        imageId = image?.id,
        kingdomId = kingdomType.kingdomId,
        saveModeId = saveMode.modeId
    )
}
