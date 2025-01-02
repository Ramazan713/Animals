package com.masterplus.animals.core.shared_features.backup.data.mapper

import com.masterplus.animals.core.shared_features.backup.data.dtos.HistoryBackup
import com.masterplus.animals.core.shared_features.backup.data.dtos.ListBackup
import com.masterplus.animals.core.shared_features.backup.data.dtos.ListSpeciesBackup
import com.masterplus.animals.core.shared_features.backup.data.dtos.SavePointBackup
import com.masterplus.animals.core.shared_features.database.entity.HistoryEntity
import com.masterplus.animals.core.shared_features.database.entity.ListEntity
import com.masterplus.animals.core.shared_features.database.entity.ListSpeciesEntity
import com.masterplus.animals.core.shared_features.database.entity.SavePointEntity


fun HistoryEntity.toHistoryBackup(): HistoryBackup {
    return HistoryBackup(
        id = id,
        content = content,
        historyTypeId = history_type_id,
        langCode = lang_code,
        modifiedDateTime = modified_date_time
    )
}

fun HistoryBackup.toHistoryEntity(): HistoryEntity{
    return HistoryEntity(
        id = id,
        content = content,
        history_type_id = historyTypeId,
        lang_code = langCode,
        modified_date_time = modifiedDateTime
    )
}



fun ListBackup.toListEntity(): ListEntity {
    return ListEntity(
        id = id,
        name = name,
        isRemovable = isRemovable,
        isArchive = isArchive,
        pos = pos
    )
}

fun ListEntity.toListBackup(): ListBackup {
    return ListBackup(
        id = id,
        name = name,
        isRemovable = isRemovable,
        isArchive = isArchive,
        pos = pos
    )
}



fun ListSpeciesEntity.toListSpeciesBackup(): ListSpeciesBackup {
    return ListSpeciesBackup(
        listId = listId,
        pos = pos,
        speciesId = speciesId,
    )
}

fun ListSpeciesBackup.toListSpeciesEntity(): ListSpeciesEntity{
    return ListSpeciesEntity(
        listId = listId,
        speciesId = speciesId,
        pos = pos
    )
}




fun SavePointEntity.toSavePointBackup(): SavePointBackup {
    return SavePointBackup(
        id = id,
        itemId = orderKey,
        saveKey = saveKey,
        destinationId = destinationId,
        saveModeId = saveModeId,
        imageId = imageId,
        destinationTypeId = destinationTypeId,
        contentTypeId = contentTypeId,
        kingdomId = kingdomId,
        titleEn = titleEn,
        titleTr = titleTr,
        modifiedTime = modifiedTime
    )
}

fun SavePointBackup.toSavePointEntity(): SavePointEntity{
    return SavePointEntity(
        id = id,
        orderKey = itemId,
        saveKey = saveKey,
        destinationId = destinationId,
        saveModeId = saveModeId,
        imageId = imageId,
        destinationTypeId = destinationTypeId,
        contentTypeId = contentTypeId,
        kingdomId = kingdomId,
        titleEn = titleEn,
        titleTr = titleTr,
        modifiedTime = modifiedTime
    )
}